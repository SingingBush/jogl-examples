package jocl.fractal;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLDevice;
import com.jogamp.opencl.CLEventList;
import com.jogamp.opencl.CLException;
import com.jogamp.opencl.gl.CLGLBuffer;
import com.jogamp.opencl.gl.CLGLContext;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLPlatform;
import com.jogamp.opencl.CLProgram;
import com.jogamp.opencl.CLProgram.CompilerOptions;
import com.jogamp.opencl.util.CLProgramConfiguration;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

import static com.jogamp.common.nio.Buffers.*;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opencl.CLMemory.Mem.*;
import static com.jogamp.opencl.CLDevice.Type.*;
import static com.jogamp.opencl.CLCommandQueue.Mode.*;
import static java.lang.Math.*;

/**
 * Computes the Mandelbrot set with OpenCL using multiple GPUs and renders the result with OpenGL.
 * A shared PBO is used as storage for the fractal image.
 * http://en.wikipedia.org/wiki/Mandelbrot_set
 * <p>
 * controls:
 * keys 1-9 control parallelism level
 * space enables/disables slice seperator
 * 'd' toggles between 32/64bit floatingpoint precision
 * mouse/mousewheel to drag and zoom
 * </p>
 * @author Michael Bien
 */
public class MultiDeviceFractal implements GLEventListener {

    private static final int FPS = 30;

    // max number of used GPUs
    private static final int MAX_PARRALLELISM_LEVEL = 8;

    // max per pixel iterations to compute the fractal
    private static final int MAX_ITERATIONS         = 500;
    public static final String JOCL_MANDLEBROT_CL = "jocl/Mandelbrot.cl";

    private static final Logger _log = Logger.getLogger(MultiDeviceFractal.class.getName());

    private static GLWindow window;

    private CLGLContext clContext;
    private CLCommandQueue[] queues;
    private CLKernel[] kernels;
    private CLProgram[] programs;
    private CLEventList probes;
    private CLGLBuffer<?>[] pboBuffers;
    private CLBuffer<IntBuffer>[] colorMap;

    private int width  = 0;
    private int height = 0;

    private double minX = -2f;
    private double minY = -1.2f;
    private double maxX  = 0.6f;
    private double maxY  = 1.3f;

    private int slices;

    private boolean drawSeperator;
    private boolean doublePrecision;
    private boolean buffersInitialized;
    private boolean rebuild;

    //private final TextRenderer textRenderer;

    public MultiDeviceFractal(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public static void main(String args[]) {
        if(!CLPlatform.isAvailable()) {
            System.out.println("OpenCL is not available");
            System.exit(1);
        }

        MultiDeviceFractal multiDeviceFractal = new MultiDeviceFractal(1024, 800);
        multiDeviceFractal.start();
    }

    public void start() {
        GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        window = GLWindow.create(capabilities);

        // Create a animator that drives canvas' display() at the specified FPS.
        final FPSAnimator animator = new FPSAnimator(window, FPS, true);

        window.addWindowListener(new com.jogamp.newt.event.WindowAdapter() {
            @Override
            public void windowDestroyNotify(com.jogamp.newt.event.WindowEvent arg0) {
                // Use a dedicate thread to run the stop() to ensure that the
                // animator stops before program exits.
                new Thread() {
                    @Override
                    public void run() {
                        animator.stop(); // stop the animator loop
                        System.exit(0);
                    }
                }.start();
            }
        });

        MouseListener mouseControls = new MouseEvents();
        window.addMouseListener(mouseControls);

        KeyListener keyControls = new KeyEvents();
        window.addKeyListener(keyControls);

        window.addGLEventListener(this);

        window.setSize(width, height);
        window.setTitle("JOCL Multi Device Mandelbrot Set - NEWT version");
        window.setVisible(true);

        //textRenderer = new TextRenderer(window.getFont().deriveFont(Font.BOLD, 14), true, true, null, false);

        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        if(clContext == null) {
            // enable GL error checking using the composable pipeline
            drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));

            drawable.getGL().glFinish();
            initCL(drawable.getContext());

            GL2 gl = drawable.getGL().getGL2();

            gl.setSwapInterval(0);
            gl.glDisable(GL_DEPTH_TEST);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

            initView(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());

            initPBO(gl);
            drawable.getGL().glFinish();

            setKernelConstants();
        }
    }

    private void initCL(GLContext glCtx){
        try {
            CLPlatform platform = CLPlatform.getDefault();
            // SLI on NV platform wasn't very fast (or did not work at all -> CL_INVALID_OPERATION)

            // the try catch is workaround for: https://jogamp.org/bugzilla/show_bug.cgi?id=964
            try {
                String icdSuffix = platform.getICDSuffix();

                if(icdSuffix.equals("NV")) {
                    clContext = CLGLContext.create(glCtx, platform.getMaxFlopsDevice(GPU));
                } else {
                    clContext = CLGLContext.create(glCtx, platform, ALL);
                }
            } catch (CLException.CLInvalidValueException e) {
                clContext = CLGLContext.create(glCtx, platform, ALL);
            }


            CLDevice[] devices = clContext.getDevices();

            slices = min(devices.length, MAX_PARRALLELISM_LEVEL);

            // create command queues for every GPU, setup colormap and init kernels
            queues = new CLCommandQueue[slices];
            kernels = new CLKernel[slices];
            probes = new CLEventList(slices);
            colorMap = new CLBuffer[slices];

            for (int i = 0; i < slices; i++) {

                colorMap[i] = clContext.createIntBuffer(32*2, READ_ONLY);
                initColorMap(colorMap[i].getBuffer(), 32, Color.BLUE, Color.GREEN, Color.RED);

                // create command queue and upload color map buffer on each used device
                queues[i] = devices[i].createCommandQueue(PROFILING_MODE).putWriteBuffer(colorMap[i], true); // blocking upload

            }

            // check if we have 64bit FP support on all devices
            // if yes we can use only one program for all devices + one kernel per device.
            // if not we will have to create (at least) one program for 32 and one for 64bit devices.
            // since there are different vendor extensions for double FP we use one program per device.
            // (OpenCL spec is not very clear about this usecases)
            boolean all64bit = true;
            for (CLDevice device : devices) {
                if(!isDoubleFPAvailable(device)) {
                    all64bit = false;
                    break;
                }
            }


            // load program(s)
            if(all64bit) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JOCL_MANDLEBROT_CL);
                programs = new CLProgram[] { clContext.createProgram(inputStream) };
            } else {
                programs = new CLProgram[slices];
                for (int i = 0; i < slices; i++) {
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JOCL_MANDLEBROT_CL);
                    programs[i] = clContext.createProgram(inputStream);
                }
            }

            buildProgram();

        } catch (IOException ex) {
            String message = String.format("can not find '%s' in classpath.", JOCL_MANDLEBROT_CL);
            _log.log(Level.SEVERE, message, ex);

            if(clContext != null) {
                clContext.release();
            }
        } catch (CLException ex) {
            _log.log(Level.SEVERE, "something went wrong, hopefully nobody got hurt", ex);
            if(clContext != null) {
                clContext.release();
            }
        }

    }

    private void initColorMap(IntBuffer colorMap, int stepSize, Color... colors) {

        for (int n = 0; n < colors.length - 1; n++) {

            Color color = colors[n];
            int r0 = color.getRed();
            int g0 = color.getGreen();
            int b0 = color.getBlue();

            color = colors[n + 1];
            int r1 = color.getRed();
            int g1 = color.getGreen();
            int b1 = color.getBlue();

            int deltaR = r1 - r0;
            int deltaG = g1 - g0;
            int deltaB = b1 - b0;

            for (int step = 0; step < stepSize; step++) {
                float alpha = (float) step / (stepSize - 1);
                int r = (int) (r0 + alpha * deltaR);
                int g = (int) (g0 + alpha * deltaG);
                int b = (int) (b0 + alpha * deltaB);
                colorMap.put((r << 16) | (g << 8) | (b << 0));
            }
        }
        colorMap.rewind();

    }

    private void initView(GL2 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, width, 0.0, height, 0.0, 1.0);
    }

    @SuppressWarnings("unchecked")
    private void initPBO(GL gl) {

        if(pboBuffers != null) {
            int[] oldPbos = new int[pboBuffers.length];
            for (int i = 0; i < pboBuffers.length; i++) {
                CLGLBuffer<?> buffer = pboBuffers[i];
                oldPbos[i] = buffer.GLID;
                buffer.release();
            }
            gl.glDeleteBuffers(oldPbos.length, oldPbos, 0);
        }

        pboBuffers = new CLGLBuffer[slices];

        int[] pbo = new int[slices];
        gl.glGenBuffers(slices, pbo, 0);

        // setup one empty PBO per slice
        for (int i = 0; i < slices; i++) {

            final int size = width*height * SIZEOF_INT / slices ;
            gl.glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pbo[i]);
            gl.glBufferData(GL_PIXEL_UNPACK_BUFFER, size, null, GL_STREAM_DRAW);
            gl.glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);

            pboBuffers[i] = clContext.createFromGLBuffer(pbo[i], size, WRITE_ONLY);
        }

        buffersInitialized = true;
    }

    private void buildProgram() {

        /*
         * workaround: The driver keeps using the old binaries for some reason.
         * to solve this we simple create a new program and release the old.
         * however rebuilding programs should be possible -> remove when drivers are fixed.
         * (again: the spec is not very clear about this kind of usages)
         */
        if(programs[0] != null && rebuild) {
            for(int i = 0; i < programs.length; i++) {
                String source = programs[i].getSource();
                programs[i].release();
                programs[i] = clContext.createProgram(source);
            }
        }

        // disable 64bit floating point math if not available
        for(int i = 0; i < programs.length; i++) {
            CLDevice device = queues[i].getDevice();

            CLProgramConfiguration configure = programs[i].prepare();
            if(doublePrecision && isDoubleFPAvailable(device)) {
                //cl_khr_fp64
                configure.withDefine("DOUBLE_FP");

                //amd's verson of double precision floating point math
                if(!device.isDoubleFPAvailable() && device.isExtensionAvailable("cl_amd_fp64")) {
                    configure.withDefine("AMD_FP");
                }
            }
            if(programs.length > 1) {
                configure.forDevice(device);
            }
            System.out.println(configure);
            configure.withOption(CompilerOptions.FAST_RELAXED_MATH).build();
        }

        rebuild = false;

        for (int i = 0; i < kernels.length; i++) {
            // init kernel with constants
            kernels[i] = programs[min(i, programs.length)].createCLKernel("mandelbrot");
        }

    }

    // init kernels with constants
    private void setKernelConstants() {
        for (int i = 0; i < slices; i++) {
            kernels[i].setForce32BitArgs(!doublePrecision || !isDoubleFPAvailable(queues[i].getDevice()))
                    .setArg(6, pboBuffers[i])
                    .setArg(7, colorMap[i])
                    .setArg(8, colorMap[i].getBuffer().capacity())
                    .setArg(9, MAX_ITERATIONS);
        }
    }

    // rendering cycle
    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // make sure GL does not use our objects before we start computing
        gl.glFinish();
        if(!buffersInitialized) {
            initPBO(gl);
            setKernelConstants();
        }
        if(rebuild) {
            buildProgram();
            setKernelConstants();
        }
        compute();

        render(gl.getGL2());
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        if(this.width == width && this.height == height)
            return;

        this.width = width;
        this.height = height;

        initPBO(drawable.getGL());
        setKernelConstants();

        initView(drawable.getGL().getGL2(), width, height);
    }

    /*
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    // OpenCL
    private void compute() {

        int sliceWidth = (int)(width / (float)slices);
        double rangeX  = (maxX - minX) / slices;
        double rangeY  = (maxY - minY);

        // release all old events, you can't reuse events in OpenCL
        probes.release();

        // start computation
        for (int i = 0; i < slices; i++) {

            kernels[i].putArg(     sliceWidth).putArg(height)
                    .putArg(minX + rangeX*i).putArg(  minY)
                    .putArg(       rangeX  ).putArg(rangeY)
                    .rewind();

            // aquire GL objects, and enqueue a kernel with a probe from the list
            queues[i].putAcquireGLObject(pboBuffers[i])
                    .put2DRangeKernel(kernels[i], 0, 0, sliceWidth, height, 0, 0, probes)
                    .putReleaseGLObject(pboBuffers[i]);

        }

        // block until done (important: finish before doing further gl work)
        for (int i = 0; i < slices; i++) {
            queues[i].finish();
        }

    }

    // OpenGL
    private void render(GL2 gl) {

        gl.glClear(GL_COLOR_BUFFER_BIT);

        //draw slices
        int sliceWidth = width / slices;

        for (int i = 0; i < slices; i++) {

            int seperatorOffset = drawSeperator?i:0;

            gl.glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pboBuffers[i].GLID);
            gl.glRasterPos2i(sliceWidth*i + seperatorOffset, 0);

            gl.glDrawPixels(sliceWidth, height, GL_BGRA, GL_UNSIGNED_BYTE, 0);

        }
        gl.glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);

        // todo: find a way to draw info to window without AWT
//        textRenderer.beginRendering(width, height, false);
//
//        textRenderer.draw("device/time/precision", 10, height-15);
//
//        for (int i = 0; i < slices; i++) {
//            CLDevice device = queues[i].getDevice();
//            boolean doubleFP = doublePrecision && isDoubleFPAvailable(device);
//            CLEvent event = probes.getEvent(i);
//            long start = event.getProfilingInfo(START);
//            long end = event.getProfilingInfo(END);
//            textRenderer.draw(device.getType().toString()+i +" "
//                    + (int)((end-start)/1000000.0f)+"ms @"
//                    + (doubleFP?"64bit":"32bit"), 10, height-(20+16*(slices-i)));
//        }
//
//        textRenderer.endRendering();
    }

    private boolean isDoubleFPAvailable(CLDevice device) {
        return device.isDoubleFPAvailable() || device.isExtensionAvailable("cl_amd_fp64");
    }

    class KeyEvents implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            short keyCode = e.getKeyCode();

            //_log.log(Level.ALL, "Key Pressed: %s", keyCode);

            if(keyCode == KeyEvent.VK_SPACE) {
                drawSeperator = !drawSeperator;
            } else if(e.getKeyChar() > '0' && e.getKeyChar() < '9') {
                int number = e.getKeyChar() - '0';
                slices = min(number, min(queues.length, MAX_PARRALLELISM_LEVEL));
                buffersInitialized = false;
            } else if(keyCode == KeyEvent.VK_D) {
                doublePrecision = !doublePrecision;
                rebuild = true;
            }
            window.display();
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

    class MouseEvents implements MouseListener {
        int _x;
        int _y;

        @Override
        public void mouseClicked(com.jogamp.newt.event.MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(com.jogamp.newt.event.MouseEvent mouseEvent) {
            _x = mouseEvent.getX();
            _y = mouseEvent.getY();
        }

        @Override
        public void mouseExited(com.jogamp.newt.event.MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(com.jogamp.newt.event.MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(com.jogamp.newt.event.MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(com.jogamp.newt.event.MouseEvent mouseEvent) {
            _x = mouseEvent.getX();
            _y = mouseEvent.getY();
        }

        @Override
        public void mouseDragged(com.jogamp.newt.event.MouseEvent e) {
            double offsetX = (_x - e.getX()) * (maxX - minX) / width;
            double offsetY = (_y - e.getY()) * (maxY - minY) / height;

            minX += offsetX;
            minY -= offsetY;

            maxX += offsetX;
            maxY -= offsetY;

            _x = e.getX();
            _y = e.getY();
        }

        @Override
        public void mouseWheelMoved(com.jogamp.newt.event.MouseEvent e) {
            float[] rotationArray = e.getRotation(); // 3 values, 2nd is one we need
            float rotation = rotationArray[1] / 25.0f;

            double deltaX = rotation * (maxX - minX);
            double deltaY = rotation * (maxY - minY);

            // offset for "zoom to cursor"
            double offsetX = (e.getX() / (float)width - 0.5f) * deltaX * 2;
            double offsetY = (e.getY() / (float)height- 0.5f) * deltaY * 2;

            minX += deltaX+offsetX;
            minY += deltaY-offsetY;

            maxX +=-deltaX+offsetX;
            maxY +=-deltaY-offsetY;

            window.display();
        }
    }
}

package elect86.gl3;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.singingbush.utils.GLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_UNSIGNED_SHORT;
import static com.jogamp.opengl.GL3.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL3.GL_COLOR;
import static com.jogamp.opengl.GL3.GL_DEPTH;
import static com.jogamp.opengl.GL3.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL3.GL_FLOAT;
import static com.jogamp.opengl.GL3.GL_NO_ERROR;
import static com.jogamp.opengl.GL3.GL_STREAM_DRAW;
import static com.jogamp.opengl.GL3.GL_UNIFORM_BUFFER;

/**
 * @author gbarbieri
 * https://github.com/java-opengl-labs/hello-triangle
 */
public class HelloTriangleSimple implements GLEventListener, KeyListener {

    private static final Logger log = LogManager.getLogger(HelloTriangleSimple.class);

    private static GLWindow window;
    private static Animator animator;

    public static void main(String[] args) {
        GLProfile.initSingleton();

        if(!GLProfile.isAvailable(GLProfile.GL3)) {
            log.error("Cannot run without OpenGL 3");
            System.exit(1);
        }

        new HelloTriangleSimple().run();
    }

    private float[] vertexData = {
            -1, -1, 1, 0, 0,
            +0, +2, 0, 0, 1,
            +1, -1, 0, 1, 0};

    private short[] elementData = {0, 2, 1};

    private interface Buffer {

        int VERTEX = 0;
        int ELEMENT = 1;
        int GLOBAL_MATRICES = 2;
        int MAX = 3;
    }

    private IntBuffer bufferName = GLBuffers.newDirectIntBuffer(Buffer.MAX);
    private IntBuffer vertexArrayName = GLBuffers.newDirectIntBuffer(1);

    private FloatBuffer clearColor = GLBuffers.newDirectFloatBuffer(4);
    private FloatBuffer clearDepth = GLBuffers.newDirectFloatBuffer(1);

    private FloatBuffer matBuffer = GLBuffers.newDirectFloatBuffer(16);

    private TriangleProgram program;

    private long start;

    private void run() {
        try {
            GLProfile glProfile = GLProfile.getMaxProgrammableCore(true); // GLProfile.get(GLProfile.GL3);

            if(!glProfile.isGL3()) {
                log.error("Was unable to get OpenGL 3 profile");
                System.exit(1);
            }

            GLCapabilities glCapabilities = new GLCapabilities(glProfile);

            window = GLWindow.create(glCapabilities);

            window.setTitle("Hello Triangle (simple)");
            window.setSize(1024, 768);

            window.setVisible(true);

            window.addGLEventListener(this);
            window.addKeyListener(this);

            animator = new Animator(window);
            animator.start();

            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyed(WindowEvent e) {
                    animator.stop();
                    System.exit(0);
                }
            });
        } catch (final GLException e) {
            log.error("Couldn't get OpenGL 3 profile", e);
            System.exit(1);
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL3 gl = drawable.getGL().getGL3();

        System.out.println(String.format("GL Implementation: %s", gl.getGLProfile().getImplName()));
        System.out.println(String.format("GL VERSION:   %s", gl.glGetString(gl.GL_VERSION)));
        System.out.println(String.format("GL VENDOR:    %s", gl.glGetString(gl.GL_VENDOR)));
        System.out.println(String.format("GL RENDERER:  %s", gl.glGetString(gl.GL_RENDERER)));

        initBuffers(gl);

        initVertexArray(gl);

        program = new TriangleProgram(gl);

        gl.glEnable(GL_DEPTH_TEST);

        start = System.currentTimeMillis();
    }

    private void initBuffers(final GL3 gl) {
        final FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertexData);
        final ShortBuffer elementBuffer = GLBuffers.newDirectShortBuffer(elementData);

        gl.glGenBuffers(Buffer.MAX, bufferName);

        gl.glBindBuffer(GL_ARRAY_BUFFER, bufferName.get(Buffer.VERTEX));
        gl.glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL_STATIC_DRAW);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferName.get(Buffer.ELEMENT));
        gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer.capacity() * Short.BYTES, elementBuffer, GL_STATIC_DRAW);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        gl.glBindBuffer(GL_UNIFORM_BUFFER, bufferName.get(Buffer.GLOBAL_MATRICES));
        gl.glBufferData(GL_UNIFORM_BUFFER, 16 * Float.BYTES * 2, null, GL_STREAM_DRAW);
        gl.glBindBuffer(GL_UNIFORM_BUFFER, 0);

        gl.glBindBufferBase(GL_UNIFORM_BUFFER, Uniform.GLOBAL_MATRICES, bufferName.get(Buffer.GLOBAL_MATRICES));

        int error = gl.glGetError();
        if (error != GL_NO_ERROR) {
            final String message = String.format("OpenGL error whilst initialising buffers: %s", GLUtils.errorCodeToString(error));
            log.fatal(message);
            throw new GLException(message);
        }
    }

    private void initVertexArray(final GL3 gl) {
        gl.glGenVertexArrays(1, vertexArrayName);
        gl.glBindVertexArray(vertexArrayName.get(0));
        {
            gl.glBindBuffer(GL_ARRAY_BUFFER, bufferName.get(Buffer.VERTEX));
            {
                int stride = (2 + 3) * Float.BYTES;
                int offset = 0;

                gl.glEnableVertexAttribArray(Attr.POSITION);
                gl.glVertexAttribPointer(Attr.POSITION, 2, GL_FLOAT, false, stride, offset);

                offset = 2 * Float.BYTES;
                gl.glEnableVertexAttribArray(Attr.COLOR);
                gl.glVertexAttribPointer(Attr.COLOR, 3, GL_FLOAT, false, stride, offset);
            }
            gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferName.get(Buffer.ELEMENT));
        }
        gl.glBindVertexArray(0);

        int error = gl.glGetError();
        if (error != GL_NO_ERROR) {
            final String message = String.format("OpenGL error whilst initialising vertex arrays: %s", GLUtils.errorCodeToString(error));
            log.fatal(message);
            throw new GLException(message);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        // view matrix
        {
            float[] view = new float[16];
            FloatUtil.makeIdentity(view);

            for (int i = 0; i < 16; i++) {
                matBuffer.put(i, view[i]);
            }
            gl.glBindBuffer(GL_UNIFORM_BUFFER, bufferName.get(Buffer.GLOBAL_MATRICES));
            gl.glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, 16 * Float.BYTES, matBuffer);
            gl.glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        gl.glClearBufferfv(GL_COLOR, 0, clearColor.put(0, 0f).put(1, .33f).put(2, 0.66f).put(3, 1f));
        gl.glClearBufferfv(GL_DEPTH, 0, clearDepth.put(0, 1f));

        gl.glUseProgram(program.getName());
        gl.glBindVertexArray(vertexArrayName.get(0));

        // model matrix
        {
            long now = System.currentTimeMillis();
            float diff = (float) (now - start) / 1_000f;

            float[] scale = FloatUtil.makeScale(new float[16], true, 0.5f, 0.5f, 0.5f);
            float[] zRotation = FloatUtil.makeRotationEuler(new float[16], 0, 0, 0, diff);
            float[] modelToWorldMat = FloatUtil.multMatrix(scale, zRotation);

            for (int i = 0; i < 16; i++) {
                matBuffer.put(i, modelToWorldMat[i]);
            }
            gl.glUniformMatrix4fv(program.getModelToWorldMatUL(), 1, false, matBuffer);
        }

        gl.glDrawElements(GL_TRIANGLES, elementData.length, GL_UNSIGNED_SHORT, 0);

        gl.glUseProgram(0);
        gl.glBindVertexArray(0);

        int error = gl.glGetError();
        if (error != GL_NO_ERROR) {
            final String message = String.format("OpenGL error: %s", GLUtils.errorCodeToString(error));
            log.fatal(message);
            throw new GLException(message);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();

        float[] ortho = new float[16];
        FloatUtil.makeOrtho(ortho, 0, false, -1, 1, -1, 1, 1, -1);
        for (int i = 0; i < 16; i++) {
            matBuffer.put(i, ortho[i]);
        }
        gl.glBindBuffer(GL_UNIFORM_BUFFER, bufferName.get(Buffer.GLOBAL_MATRICES));
        gl.glBufferSubData(GL_UNIFORM_BUFFER, 0, 16 * Float.BYTES, matBuffer);
        gl.glBindBuffer(GL_UNIFORM_BUFFER, 0);

        gl.glViewport(x, y, width, height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        final GL3 gl = drawable.getGL().getGL3();

        gl.glDeleteProgram(program.getName());
        gl.glDeleteVertexArrays(1, vertexArrayName);
        gl.glDeleteBuffers(Buffer.MAX, bufferName);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                log.info("todo"); // todo: pause the rotation when key pressed
                break;
            case KeyEvent.VK_ESCAPE:
                new Thread(() -> window.destroy()).start();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private interface Attr {
        int POSITION = 0;
        int COLOR = 1;
        int NORMAL = 2;
        int TEXCOORD = 3;
        int DRAW_ID = 4;
    }

    private interface Uniform {
        int MATERIAL = 0;
        int TRANSFORM0 = 1;
        int TRANSFORM1 = 2;
        int INDIRECTION = 3;
        int GLOBAL_MATRICES = 4;
        int CONSTANT = 0;
        int PER_FRAME = 1;
        int PER_PASS = 2;
        int LIGHT = 3;
    }

    private interface Stream {
        int A = 0;
        int B = 1;
    }
}

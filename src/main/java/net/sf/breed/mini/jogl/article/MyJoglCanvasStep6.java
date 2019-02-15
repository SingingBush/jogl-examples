package net.sf.breed.mini.jogl.article;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.DebugGL2; // was import javax.media.opengl.DebugGL;
import com.jogamp.opengl.GL2; // was import javax.media.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable; // was import javax.media.opengl.GLAutoDrawable;
import com.jogamp.opengl.awt.GLCanvas; // was import javax.media.opengl.GLCanvas;
import com.jogamp.opengl.GLCapabilities; // was import javax.media.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener; // was import javax.media.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile; // this is new
import com.jogamp.opengl.glu.GLU; // was javax.media.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric; // was javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator; // was com.sun.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture; // was com.sun.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData; // was com.sun.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO; // was com.sun.opengl.util.texture.TextureIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A minimal JOGL demo.
 *
 * This file was adapted from the article on http://www.land-of-kain.de/docs/jogl/
 *
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 26 Feb 2009
 */
public class MyJoglCanvasStep6 extends GLCanvas implements GLEventListener {

    private static final Logger log = LogManager.getLogger(MyJoglCanvasStep6.class);

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The GL unit (helper class). */
    private GLU glu;

    /** The frames per second setting. */
    private int fps = 60;

    /** The OpenGL animator. */
    private FPSAnimator animator;

    /** The earth texture. */
    private Texture earthTexture;

    /** The angle of the satellite orbit (0..359). */
    private float satelliteAngle = 0;

    /** The texture for a solar panel. */
    private Texture solarPanelTexture;

    /**
     * A new mini starter.
     *
     * @param capabilities The GL capabilities.
     * @param width The window width.
     * @param height The window height.
     */
    public MyJoglCanvasStep6(GLCapabilities capabilities, int width, int height) {
        addGLEventListener(this);
    }

    /**
     * @return Some standard GL capabilities (with alpha).
     */
    private static GLCapabilities createGLCapabilities() {
        //GLCapabilities capabilities = new GLCapabilities();
        final GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(glProfile);

        capabilities.setRedBits(8);
        capabilities.setBlueBits(8);
        capabilities.setGreenBits(8);
        capabilities.setAlphaBits(8);
        return capabilities;
    }

    /**
     * Sets up the screen.
     *
     * @see com.jogamp.opengl.GLEventListener#init(com.jogamp.opengl.GLAutoDrawable)
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        drawable.setGL(new DebugGL2(gl));

        // Enable z- (depth) buffer for hidden surface removal.
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        // Enable smooth shading.
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Define "clear" color.
        gl.glClearColor(0f, 0f, 0f, 0f);

        // We want a nice perspective.
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        // Create GLU.
        glu = new GLU();

        // Load earth texture.
        earthTexture = loadTexture("images/land-of-kain/earthmap1k.jpg", false);
//        try {
//            InputStream stream = getClass().getResourceAsStream("earth_1024x512.png");
//            TextureData data = TextureIO.newTextureData(stream, false, "png");
//            earthTexture = TextureIO.newTexture(data);
//        }
//        catch (IOException exc) {
//            exc.printStackTrace();
//            System.exit(1);
//        }

        // Load the solar panel texture.
        solarPanelTexture = loadTexture("images/land-of-kain/solar_panel_256x32.png", false);
//        try {
//            InputStream stream = getClass().getResourceAsStream("solar_panel_256x32.png");
//            TextureData data = TextureIO.newTextureData(stream, false, "png");
//            solarPanelTexture = TextureIO.newTexture(data);
//        }
//        catch (IOException exc) {
//            exc.printStackTrace();
//            System.exit(2);
//        }

        // Start animator.
        animator = new FPSAnimator(this, fps);
        animator.start();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        // todo: this wasn't in the original file
    }

    /**
     * The only method that you should implement by yourself.
     *
     * @see com.jogamp.opengl.GLEventListener#display(com.jogamp.opengl.GLAutoDrawable)
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        if (!animator.isAnimating()) {
            return;
        }
        final GL2 gl = drawable.getGL().getGL2();

        // Clear screen.
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Set camera.
        setCamera(gl, glu, 30);

        // Prepare light parameters.
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {-30, 0, 0, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

        // Enable lighting in GL2.
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);

        // Set material properties.
        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

        // Apply texture.
//        earthTexture.enable();
//        earthTexture.bind();
        earthTexture.enable(gl);  // same as gl.glEnable(earthTexture.getTarget());
        earthTexture.bind(gl);  // same as gl.glBindTexture(earthTexture.getTarget(), earthTexture.getTextureObject());

        // Draw sphere (possible styles: FILL, LINE, POINT).
        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricTexture(earth, true);
        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
        glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
        final float radius = 6.378f;
        final int slices = 16;
        final int stacks = 16;
        glu.gluSphere(earth, radius, slices, stacks);
        glu.gluDeleteQuadric(earth);

        // Save old state.
        gl.glPushMatrix();

        // Compute satellite position.
        satelliteAngle = (satelliteAngle + 1f) % 360f;
        final float distance = 10.000f;
        final float x = (float) Math.sin(Math.toRadians(satelliteAngle)) * distance;
        final float y = (float) Math.cos(Math.toRadians(satelliteAngle)) * distance;
        final float z = 0;
        gl.glTranslatef(x, y, z);
        gl.glRotatef(satelliteAngle, 0, 0, -1);
        gl.glRotatef(45f, 0, 1, 0);

        // Set silver color, and disable texturing.
        gl.glDisable(GL2.GL_TEXTURE_2D);
        float[] ambiColor = {0.3f, 0.3f, 0.3f, 1f};
        float[] specColor = {0.8f, 0.8f, 0.8f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambiColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specColor, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 90f);

        // Draw satellite body.
        final float cylinderRadius = 1f;
        final float cylinderHeight = 2f;
        final int cylinderSlices = 16;
        final int cylinderStacks = 16;
        GLUquadric body = glu.gluNewQuadric();
        glu.gluQuadricTexture(body, false);
        glu.gluQuadricDrawStyle(body, GLU.GLU_FILL);
        glu.gluQuadricNormals(body, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(body, GLU.GLU_OUTSIDE);
        gl.glTranslatef(0, 0, -cylinderHeight / 2);
        glu.gluDisk(body, 0, cylinderRadius, cylinderSlices, 2);
        glu.gluCylinder(body, cylinderRadius, cylinderRadius, cylinderHeight, cylinderSlices, cylinderStacks);
        gl.glTranslatef(0, 0, cylinderHeight);
        glu.gluDisk(body, 0, cylinderRadius, cylinderSlices, 2);
        glu.gluDeleteQuadric(body);
        gl.glTranslatef(0, 0, -cylinderHeight / 2);

        // Set white color, and enable texturing.
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0f);

        // Draw solar panels.
        gl.glScalef(6f, 0.7f, 0.1f);

        //solarPanelTexture.bind();
        solarPanelTexture.bind(gl);  // same as gl.glBindTexture(solarPanelTexture.getTarget(), solarPanelTexture.getTextureObject());

        gl.glBegin(GL2.GL_QUADS);
        final float[] frontUL = {-1.0f, -1.0f, 1.0f};
        final float[] frontUR = {1.0f, -1.0f, 1.0f};
        final float[] frontLR = {1.0f, 1.0f, 1.0f};
        final float[] frontLL = {-1.0f, 1.0f, 1.0f};
        final float[] backUL = {-1.0f, -1.0f, -1.0f};
        final float[] backLL = {-1.0f, 1.0f, -1.0f};
        final float[] backLR = {1.0f, 1.0f, -1.0f};
        final float[] backUR = {1.0f, -1.0f, -1.0f};
        // Front Face.
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(frontUR, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(frontUL, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(frontLL, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(frontLR, 0);
        // Back Face.
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(backUL, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(backUR, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(backLR, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(backLL, 0);
        gl.glEnd();

        // Restore old state.
        gl.glPopMatrix();
    }

    /**
     * Resizes the screen.
     *
     * @see com.jogamp.opengl.GLEventListener#reshape(com.jogamp.opengl.GLAutoDrawable,
     *      int, int, int, int)
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    /*
     * I presume this used to be on GLEventListener but it's no longer used.
     * Changing devices is not supported.
     *
     * @see com.jogamp.opengl.GLEventListener#displayChanged(com.jogamp.opengl.GLAutoDrawable,
     *      boolean, boolean)
     */
//    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
//        throw new UnsupportedOperationException("Changing display is not supported.");
//    }

    /**
     * @param gl The GL context.
     * @param glu The GL unit.
     * @param distance The distance from the screen.
     */
    private void setCamera(GL2 gl, GLU glu, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Starts the JOGL mini demo.
     *
     * @param args Command line args.
     */
    public final static void main(String[] args) {
        final GLCapabilities capabilities = createGLCapabilities();
        final MyJoglCanvasStep6 canvas = new MyJoglCanvasStep6(capabilities, 800, 500);
        final JFrame frame = new JFrame("Mini JOGL Demo (breed)");
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        canvas.requestFocus();
    }

    @Nullable
    private Texture loadTexture(@NotNull final String resourceFile, final boolean mipmap) {
        try(final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceFile)) {
            return stream != null ? TextureIO.newTexture(stream, mipmap, null) : null;
        } catch (IOException e) {
            log.error("Couldn't load earth texture", e);
            System.exit(1);
        }
        return null;
    }
}

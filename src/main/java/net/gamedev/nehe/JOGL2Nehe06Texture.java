package net.gamedev.nehe;

import java.io.IOException;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.singingbush.core.Quad;
import com.singingbush.core.PolygonMesh;
import com.singingbush.loaders.SimpleModelLoader;
import com.singingbush.utils.ResourceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jogamp.opengl.GL.*;  // GL constants
import static com.jogamp.opengl.GL2.*; // GL2 constants

/**
 * NeHe Lesson #6 (JOGL 2 Port): Texture
 * @author Hock-Chuan Chua
 * @version May 2012
 */
@SuppressWarnings("serial")
public class JOGL2Nehe06Texture implements GLEventListener {

    private static final Logger log = LogManager.getLogger(JOGL2Nehe06Texture.class);
    // Setup OpenGL Graphics Renderer

    private GLU glu;  // for the GL Utility
    // Rotational angle about the x, y and z axes in degrees
    private static float angleX = 0.0f;
    private static float angleY = 0.0f;
    private static float angleZ = 0.0f;
    // Rotational speed about x, y, z axes in degrees per refresh
    private static float rotateSpeedX = 0.3f;
    private static float rotateSpeedY = 0.2f;
    private static float rotateSpeedZ = 0.4f;

    private PolygonMesh<Quad> cubeModel; // model
    
    // Texture
    private Texture texture;
    private static final String TEXTURE_FILE_NAME = "images/nehe.png";
    private static final String TEXTURE_FILE_TYPE = ".png";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureTop, textureBottom, textureLeft, textureRight;


    // ------ Implement methods declared in GLEventListener ------

    /*
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context

        log.info(String.format("GL Implementation: %s", gl.getGLProfile().getImplName()));
        log.info(String.format("GL VENDOR:    %s", gl.glGetString(GL2.GL_VENDOR)));
        log.info(String.format("GL RENDERER:  %s", gl.glGetString(GL2.GL_RENDERER)));
        log.info(String.format("GL VERSION:   %s", gl.glGetString(GL2.GL_VERSION)));

        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting


        // Read the world
        cubeModel = SimpleModelLoader.loadQuadMesh("models/cube.txt");
        if(cubeModel == null) {
            System.err.println("Unable to load cube model");
            System.exit(1);
        }
        
        // Load texture from image
        try {
            // Create a OpenGL Texture object from (URL, mipmap, file suffix)
            // Use URL so that can read from JAR and disk file.
            texture = ResourceLoader.loadTextureResource(TEXTURE_FILE_NAME, false);

            // Use linear filter for texture if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Use linear filter for texture if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            // Texture image flips vertically. Shall use TextureCoords class to retrieve
            // the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
            TextureCoords textureCoords = texture.getImageTexCoords();
            textureTop = textureCoords.top();
            textureBottom = textureCoords.bottom();
            textureLeft = textureCoords.left();
            textureRight = textureCoords.right();
        } catch (GLException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    /*
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        // ------ Render a Cube with texture ------
        gl.glLoadIdentity();  // reset the model-view matrix
        gl.glTranslatef(0.0f, 0.0f, -5.0f); // translate into the screen
        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
        gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f); // rotate about the z-axis

        // Enables this texture's target in the current GL context's state.
        texture.enable(gl);  // same as gl.glEnable(texture.getTarget());
        // gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        // Binds this texture to the current GL context.
        texture.bind(gl);  // same as gl.glBindTexture(texture.getTarget(), texture.getTextureObject());

        // Process each square
        for (final Quad quad : cubeModel.getPolygons()) {
            gl.glBegin(GL_QUADS);
            //gl.glNormal3f(0.0f, 0.0f, 1.0f); // Normal pointing out of screen

            // need to flip the image
            float textureHeight = textureTop - textureBottom;
            float u, v;

            u = quad.getVertices()[0].getU();
            v = quad.getVertices()[0].getV() * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);

            gl.glVertex3f(quad.getVertices()[0].getX(), quad.getVertices()[0].getY(), quad.getVertices()[0].getZ());

            u = quad.getVertices()[1].getU();
            v = quad.getVertices()[1].getV() * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);

            gl.glVertex3f(quad.getVertices()[1].getX(), quad.getVertices()[1].getY(), quad.getVertices()[1].getZ());

            u = quad.getVertices()[2].getU();
            v = quad.getVertices()[2].getV() * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);

            gl.glVertex3f(quad.getVertices()[2].getX(), quad.getVertices()[2].getY(), quad.getVertices()[2].getZ());

            u = quad.getVertices()[3].getU();
            v = quad.getVertices()[3].getV() * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);

            gl.glVertex3f(quad.getVertices()[3].getX(), quad.getVertices()[3].getY(), quad.getVertices()[3].getZ());

            gl.glEnd();
        }


        // Disables this texture's target (e.g., GL_TEXTURE_2D) in the current GL
        // context's state.
        //texture.disable(gl);  // same as gl.glDisable(texture.getTarget());

        // Update the rotational angel after each refresh by the corresponding
        // rotational speed
        angleX += rotateSpeedX;
        angleY += rotateSpeedY;
        angleZ += rotateSpeedZ;
    }

    /*
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) { }
}


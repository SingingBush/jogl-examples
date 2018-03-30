package com.singingbush;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class can be used to launch either a GL4 or GL3 renderer depending on the available hardware
 * @author Samael Bate (singingbush)
 * created on 30/03/18
 */
public class NewtLauncher {

    private static final Logger log = LogManager.getLogger(NewtLauncher.class);

    private static final String TITLE = "JOGL NEWT Window";
    private static final int WINDOW_WIDTH = 640;  // default width of the window
    private static final int WINDOW_HEIGHT = 480; // default height of the window
    private static final int FPS = 60; // animator's target frames per second

    public static void main(String[] args) {
        GLProfile.initSingleton();

        log.debug(GLProfile.glAvailabilityToString());

        if(!(GLProfile.isAvailable(GLProfile.GL4) || GLProfile.isAvailable(GLProfile.GL3))) {
            System.err.println("Cannot run without OpenGL 4 or 3");
            System.exit(1);
        }

        try {
            // Default profile on OSX is GL2, we need to set it to get the maximum available: 4.1 on Mavericks
            final GLProfile glp = GLProfile.getMaxProgrammableCore(true);

            log.info("OpenGL profile: {}", glp.getName());

            final GLCapabilities caps = new GLCapabilities(glp);

            final GLWindow window = GLWindow.create(caps);
            window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            window.setTitle(TITLE);

            if(glp.isGL4()) {
                log.info("Using GL4 version of HelloTriangleSimple");
                final elect86.gl4.HelloTriangleSimple gl4Renderer = new elect86.gl4.HelloTriangleSimple();
                window.addGLEventListener(gl4Renderer);
                window.addKeyListener(gl4Renderer);
            } else if(glp.isGL3()) {
                log.info("Using GL3 version of HelloTriangleSimple");
                final elect86.gl3.HelloTriangleSimple gl3Renderer = new elect86.gl3.HelloTriangleSimple();
                window.addGLEventListener(gl3Renderer);
                window.addKeyListener(gl3Renderer);
            } else {
                log.error("Was unable to get OpenGL 4 or 3 profile");
                System.exit(1);
            }

            final FPSAnimator animator = new FPSAnimator(window, FPS, true);

            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyNotify(WindowEvent arg0) {
                    // Use a dedicate thread to run the stop() to ensure that the
                    // animator stops before program exits.
                    new Thread(() -> {
                        animator.stop(); // stop the animator loop
                        System.exit(0);
                    }).start();
                }
            });

            window.setVisible(true);
            animator.start();
        } catch (final GLException e) {
            log.error("Couldn't get OpenGL profile", e);
            System.exit(1);
        }
    }
}

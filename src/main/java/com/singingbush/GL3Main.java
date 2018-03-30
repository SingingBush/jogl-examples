package com.singingbush;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import elect86.gl3.HelloTriangleSimple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Samael Bate (singingbush)
 * created on 30/03/18
 */
public class GL3Main {

    private static final Logger log = LogManager.getLogger(GL3Main.class);

    public static void main(String[] args) {
        GLProfile.initSingleton();

        if(!GLProfile.isAvailable(GLProfile.GL3)) {
            log.error("Cannot run without OpenGL 3");
            System.exit(1);
        }

        try {
            // Default profile on OSX is GL2, we need to set it to get the maximum available: 4.1 on Mavericks
            final GLProfile glp = GLProfile.get(GLProfile.GL3);  // GLProfile glp = GLProfile.getMaximum(true);

            log.info("OpenGL profile: {}", glp.getName());

            if(!glp.isGL3()) {
                log.error("Was unable to get OpenGL 3 profile");
                System.exit(1);
            }

            final GLCapabilities capabilities = new GLCapabilities(glp);

            final GLWindow glWindow = GLWindow.create(capabilities);

            final FPSAnimator animator = new FPSAnimator(glWindow, 60, true);

            glWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyNotify(WindowEvent arg0) {
                    // Use a dedicate thread to run the stop() to ensure that the
                    // animator stops before program exits.
                    new Thread(() -> {
                        animator.stop(); // stop the animator loop
                        System.exit(0);
                    }).start();
                };
            });

            final GLEventListener renderer = new HelloTriangleSimple(); // new com.singingbush.gl3.GL3Simple();

            glWindow.addGLEventListener(renderer);
            glWindow.setTitle("OpenGL 3 - JOGL NEWT Window");
            glWindow.setSize(640, 480);
            glWindow.setVisible(true);

            animator.start();
        } catch (final GLException e) {
            log.error("Couldn't get OpenGL 3 profile", e);
            System.exit(1);
        }

    }
}

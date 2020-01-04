package elect86.gl3;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.singingbush.utils.GLUtils;
import com.singingbush.utils.ResourceLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import java.io.PrintStream;

import static com.jogamp.opengl.GL.GL_NO_ERROR;

/**
 * @author Samael Bate (singingbush)
 * created on 04/01/2020
 */
public class TriangleProgram {

    private static final Logger log = LogManager.getLogger(TriangleProgram.class);

    private static final int GLOBAL_MATRICES = 4;

    private final int name, modelToWorldMatUL;

    TriangleProgram(GL3 gl) {
        final ShaderCode vertShader = ResourceLoader
            .vertexShader(gl, "shaders/gl3/hello-triangle", "vert");

        final ShaderCode fragShader = ResourceLoader.
            fragmentShader(gl, "shaders/gl3/hello-triangle", "frag");

        final ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.add(vertShader);
        shaderProgram.add(fragShader);
        shaderProgram.init(gl);

        name = shaderProgram.program();

        final PrintStream errLog = IoBuilder.forLogger(gl.getClass())
            .setLevel(Level.ERROR)
            .buildPrintStream();

        shaderProgram.link(gl, errLog);

        modelToWorldMatUL = gl.glGetUniformLocation(name, "model");

        if (modelToWorldMatUL == -1) {
            log.error("uniform 'model' not found!");
        }

        int globalMatricesBI = gl.glGetUniformBlockIndex(name, "GlobalMatrices");

        if (globalMatricesBI == -1) {
            log.error("block index 'GlobalMatrices' not found!");
        }
        gl.glUniformBlockBinding(name, globalMatricesBI, GLOBAL_MATRICES);

        int error = gl.glGetError();
        if (error != GL_NO_ERROR) {
            final String message = String.format("OpenGL error: %s", GLUtils.errorCodeToString(error));
            log.fatal(message);
            throw new GLException(message);
        }
    }

    public int getName() {
        return name;
    }

    public int getModelToWorldMatUL() {
        return modelToWorldMatUL;
    }
}

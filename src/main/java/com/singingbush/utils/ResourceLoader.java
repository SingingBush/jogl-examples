package com.singingbush.utils;

import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static com.jogamp.opengl.GL3ES3.GL_GEOMETRY_SHADER;

/**
 * @author Samael Bate (singingbush)
 * created on 30/03/18
 */
public class ResourceLoader {

    @Nullable
    public static BufferedImage loadImageResource(@NotNull final String resourceFile) throws IOException {
        final URL resource = ResourceLoader.class.getClassLoader().getResource(resourceFile);

        return resource != null ? ImageIO.read(resource) : null;
    }

    @Nullable
    public static Texture loadTextureResource(@NotNull final String resourceFile, final boolean mipmap) throws IOException {
        final URL resource = ResourceLoader.class.getClassLoader().getResource(resourceFile);
        return resource != null ? TextureIO.newTexture(resource, mipmap, null) : null;
    }

    @Deprecated
    public static ShaderCode vertexShader(@NotNull GL2ES2 gl, @NotNull final String resourceFile, @Nullable final String extension) {
        return ShaderCode.create(gl, GL_VERTEX_SHADER, ResourceLoader.class,
                null, null, resourceFile, extension, null, true);
    }

    @Deprecated
    public static ShaderCode fragmentShader(@NotNull GL2ES2 gl, @NotNull final String resourceFile, @Nullable final String extension) {
        return ShaderCode.create(gl, GL_FRAGMENT_SHADER, ResourceLoader.class,
                null, null, resourceFile, extension, null, true);
    }

    public static ShaderBuilder shader() {
        return new ShaderBuilder();
    }

    public static final class ShaderBuilder {
        private Class<?> ctx = ResourceLoader.class;
        private GL2ES2 gl;
        private String path;
        private String filename;
        private String extension;
        private boolean mutable = false;

        private ShaderBuilder() {}

        public ShaderBuilder withContext(final Class<?> clazz) {
            this.ctx = clazz;
            return this;
        }

        public ShaderBuilder withProfile(@NotNull final GL2ES2 gl) {
            this.gl = gl;
            return this;
        }

        public ShaderBuilder withPath(final String path) {
            this.path = path;
            return this;
        }

        public ShaderBuilder withFilename(final String filename) {
            this.filename = filename;
            return this;
        }

        public ShaderBuilder withExtension(final String extension) {
            this.extension = extension;
            return this;
        }

        public ShaderBuilder withMutableStringBuilder(final boolean mutable) {
            this.mutable = mutable;
            return this;
        }

        public ShaderCode buildVertexShader() {
            return build(GL_VERTEX_SHADER);
        }

        public ShaderCode buildFragmentShader() {
            return build(GL_FRAGMENT_SHADER);
        }

        public ShaderCode buildGeometryShader() {
            return build(GL_GEOMETRY_SHADER);
        }

        private ShaderCode build(final int type) {
            return ShaderCode.create(this.gl, type, this.ctx,
                this.path, null,
                this.filename,
                this.extension, null,
                this.mutable);
        }

    }
}

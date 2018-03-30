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

    public static ShaderCode vertexShader(GL2ES2 gl, @NotNull final String resourceFile, @Nullable final String extension) {
        return ShaderCode.create(gl, GL_VERTEX_SHADER, ResourceLoader.class,
                null, null, resourceFile, extension, null, true);
    }

    public static ShaderCode fragmentShader(GL2ES2 gl, @NotNull final String resourceFile, @Nullable final String extension) {
        return ShaderCode.create(gl, GL_FRAGMENT_SHADER, ResourceLoader.class,
                null, null, resourceFile, extension, null, true);
    }
}

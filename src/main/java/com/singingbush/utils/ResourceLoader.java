package com.singingbush.utils;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author Samael Bate (singingbush)
 * created on 30/03/18
 */
public class ResourceLoader {

    @Nullable
    public static BufferedImage loadImageResource(final String resourceFile) throws IOException {
        final URL resource = ResourceLoader.class.getClassLoader().getResource(resourceFile);

        return resource != null ? ImageIO.read(resource) : null;
    }

    public static Texture loadTextureResource(final String resourceFile, final boolean mipmap) throws IOException {
        final URL resource = ResourceLoader.class.getClassLoader().getResource(resourceFile);
        return resource != null ? TextureIO.newTexture(resource, mipmap, null) : null;
    }
}

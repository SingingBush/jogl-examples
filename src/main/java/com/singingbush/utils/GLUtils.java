package com.singingbush.utils;

import static com.jogamp.opengl.GL.*;

/**
 * @author Samael Bate (singingbush)
 * created on 04/01/2020
 */
public class GLUtils {

    public static String errorCodeToString(final int errorCode) {
        String errorString;

        switch (errorCode) {
            case GL_NO_ERROR:
                errorString = "GL_NO_ERROR";
                break;
            case GL_INVALID_ENUM:
                errorString = "GL_INVALID_ENUM";
                break;
            case GL_INVALID_VALUE:
                errorString = "GL_INVALID_VALUE";
                break;
            case GL_INVALID_OPERATION:
                errorString = "GL_INVALID_OPERATION";
                break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                errorString = "GL_INVALID_FRAMEBUFFER_OPERATION";
                break;
            case GL_OUT_OF_MEMORY:
                errorString = "GL_OUT_OF_MEMORY";
                break;
            default:
                errorString = "UNKNOWN";
                break;
        }
        return errorString;
    }
}

package me.trolca.utils;

/**
 * This is called when there's an error compiling an openGL shader.
 */
public class ShaderCompilationException extends Exception {


    public ShaderCompilationException(String message) {
        super(message);
    }

}

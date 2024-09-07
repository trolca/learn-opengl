package me.trolca.jade.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class ShaderUtils {

    private static final File[] shaders = new File("assets/shaders").listFiles();

    /**
     * Returns the contents of a shader as a array of string.
     * If the shader contains 2 shaders it is going to be assumed that the
     * first one is the vertex shader and the second one is the fragment shader.
     * @param shaderName The name of the file of the shader.
     * @return An array of the contents, where if in the file there are 2 shaders contains: <br>
     *      0 - The vertex shader <br>
     *      1 - The fragment shader.
     * @throws IOException On error while reading the file
     */
    public static String[] getShaders(String shaderName) throws IOException {
        File shaderFile = null;

        for(File file : shaders){
            if(file.getName().equals(shaderName)){
                shaderFile = file;
                break;
            }
        }

        if(shaderFile == null)
            return null;

        FileInputStream fis = new FileInputStream(shaderFile);
        byte[] bytes = fis.readAllBytes();

        String contents = Utils.bytesToString(bytes);
        String[] shaders = contents.split("}");
        if(shaders.length < 2) return new String[]{shaders[0] + "}"};

        shaders[0] = shaders[0] + "}";
        shaders[1] = shaders[1] + "}";

        fis.close();

        return shaders;
    }

}

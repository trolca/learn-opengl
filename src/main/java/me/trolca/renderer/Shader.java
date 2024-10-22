package me.trolca.renderer;

import me.trolca.utils.ShaderCompilationException;
import me.trolca.utils.Utils;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgram;
    private boolean beingUsed = false;

    private String vertexSrc;
    private String fragmentSrc;

    public Shader(String filepath){
        try{

            File shaderFile = new File(filepath);
            if(!shaderFile.exists()){
                throw new IOException("This file doesn't exists!");
            }
            String source = Utils.getTextFromInputStream(new FileInputStream(shaderFile));
            String[] shaders = source.split("#type");
            for (String shader : shaders) {
                shader = shader.trim();
                if (shader.toLowerCase().startsWith("vertex")) {
                    vertexSrc = shader.substring(6);
                } else if (shader.toLowerCase().startsWith("fragment")) {
                    fragmentSrc = shader.substring(8);
                }
            }
            if(vertexSrc == null){
                throw new IOException("No vertex shader detected in '" + filepath + "' check for typos!");
            }else if(fragmentSrc == null){
                throw new IOException("No fragment shader detected in '" + filepath + "' check for typos!");
            }
            compile();
        }catch (IOException | ShaderCompilationException e){
            System.out.println("Error with: " + filepath);
            e.printStackTrace(System.out);
        }

    }

    /**
     * Compiles the shader
     */
    private void compile() throws ShaderCompilationException {

        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader code to the GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        //Check if error in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            throw new ShaderCompilationException("ERROR: \n\tVertex shader compilation failed\n\t"
                    + glGetShaderInfoLog(vertexID, len) );
        }

        //Load and compile the fragment shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader code to the GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        //Check if error in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            throw new ShaderCompilationException("ERROR: \n\tVertex shader compilation failed\n\t"
                    + glGetShaderInfoLog(vertexID, len) );
        }

        this.shaderProgram = glCreateProgram();
        glAttachShader(this.shaderProgram, vertexID);
        glAttachShader(this.shaderProgram, fragmentID);
        glLinkProgram(this.shaderProgram);

        success = glGetProgrami(this.shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(this.shaderProgram, GL_INFO_LOG_LENGTH);
            throw new ShaderCompilationException("ERROR: \n\tLinking shaders failed\n\t"
                    + glGetShaderInfoLog(this.shaderProgram, len) );
        }

    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(12);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }


    public void uploadVec2f(String varName, Vector2f vec){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadTextureArray(String varName, int[] slots){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1iv(varLocation, slots);
    }

    /**
     * Sets the current render process to this shader.
     */
    public void use(){
        if(!beingUsed) {
            glUseProgram(this.shaderProgram);
            beingUsed = true;
        }
    }

    /**
     * Detaches the shader from the current render process.
     */
    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }


}

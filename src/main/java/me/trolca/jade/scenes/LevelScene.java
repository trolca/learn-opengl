package me.trolca.jade.scenes;

import me.trolca.jade.Window;
import me.trolca.jade.utils.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class LevelScene extends Scene{

    private String[] shaders;

    private int vertexID, fragmentID, shaderProgram;

    private int vaoID, vboID, eboID;


    private float[] verticesArray = {
            //Position              //color
            -0.5f, -0.1f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, //Bottom left
            -0.5f,  0.1f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, //Top left
             0.5f,  0.1f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, //Top right
             0.5f, -0.1f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f //Bottom right
    };

    private int[] elements = {
            3, 1, 2,
            3, 0, 1
    };


    public LevelScene(){
        try {
            this.shaders = ShaderUtils.getShaders("default.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Inside level scene");
        init();
    }

    @Override
    protected void init() {

        //Creates vertex shader - vertices
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexID, shaders[0]);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == 0){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println(len);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //Creates fragment shader - colors
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID, shaders[1]);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == 0){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println(len);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == 0){
            int len = glGetShaderi(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println(len);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tProgram linking failed");
            System.out.println(glGetShaderInfoLog(shaderProgram, len));
            assert false : "";
        }

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(verticesArray.length);
        vertexBuffer.put(verticesArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length);
        elementBuffer.put(elements).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int floatByteSize = Float.BYTES;
        int attributesSize = (positionSize + colorSize) * floatByteSize;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, attributesSize, 0); //stride - the offset of every vertex and it's attributes
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, attributesSize, positionSize * floatByteSize);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {

        //We bing everything to draw it
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elements.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }


}

package me.trolca.jade.scenes;

import me.trolca.jade.KeyListener;
import me.trolca.jade.Window;
import me.trolca.jade.utils.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.ThreadLocalUtil;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private String[] shaders;

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
      //position                   //color
       0.5f, -0.5f, 0.0f,           1.0f, 0.0f, 0.0f, 1.0f, //Bottom right
      -0.5f, 0.5f, 0.0f,            0.0f, 1.0f, 0.0f, 1.0f, //Top left
       0.5f, 0.5f, 0.0f,            0.0f, 0.0f, 1.0f, 1.0f, //Top right
      -0.5f, -0.5f, 0.0f,           1.0f, 1.0f, 0.0f, 1.0f  //Bottom left
    };

    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, //Top right triangle
        0, 1, 3
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene(){
        try {
            this.shaders = ShaderUtils.getShaders("default.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Inside level editor scene");
        init();
    }

    @Override
    protected void init() {
        //Compile and link shaders

        //Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader code to the GPU
        glShaderSource(vertexID, this.shaders[0]);
        glCompileShader(vertexID);

        //Check if error in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println(len);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //Load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader code to the GPU
        glShaderSource(fragmentID, shaders[1]);
        glCompileShader(fragmentID);

        //Check if error in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        //Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: linking \n\tLinking shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        // VAO, VBO, EBO, buffer objects and send them to the GPU
        // VAO - Vertex Array Object, this is where all the geometry is stored
        // VBO - Vertex Buffer Object, this is where all the vertices are stored and their attributes
        // EBO - Element Buffer Object, this is the order in which the GPU renders the vertices

        //NEVER USE GLES FOR THIS!!!!!!!!!!!!!!!
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload it
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        //Create EBO upload
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = Float.BYTES;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //Bind shader program
        glUseProgram(shaderProgram);
        //Bind the VAO that we're using
        glBindVertexArray(vaoID);

        //Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everytrhing
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }

}

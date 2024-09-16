package me.trolca.jade.scenes;

import me.trolca.jade.Camera;
import me.trolca.jade.KeyListener;
import me.trolca.renderer.Shader;
import me.trolca.utils.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
      //position                   //color
       100.5f, 0.5f, 0.0f,           1.0f, 0.0f, 0.0f, 1.0f, //Bottom right
      -0.5f, 100.5f, 0.0f,            0.0f, 1.0f, 0.0f, 1.0f, //Top left
       100.5f, 100.5f, 0.0f,            0.0f, 0.0f, 1.0f, 1.0f, //Top right
      -0.5f, 0.5f, 0.0f,           1.0f, 1.0f, 0.0f, 1.0f  //Bottom left
    };

    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, //Top right triangle
        0, 1, 3
    };

    private Shader shader;

    private int vaoID, vboID, eboID;

    public LevelEditorScene(){
        this.shader = new Shader("assets\\shaders\\default.glsl");
        System.out.println("Inside level editor scene");
        init();
    }

    @Override
    protected void init() {

        this.camera = new Camera(new Vector2f(0.0f,0.0f));

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

        if(KeyListener.isKeyPressed(KeyEvent.VK_W)){
            camera.position.add(0,  dt*500.0f);
        }else if(KeyListener.isKeyPressed(KeyEvent.VK_S)){
            camera.position.sub(0, dt*500.0f);
        }else if(KeyListener.isKeyPressed(KeyEvent.VK_A)){
            camera.position.sub(dt*500, 0);
        }else if(KeyListener.isKeyPressed(KeyEvent.VK_D)){
            camera.position.add(dt*500, 0);
        }

        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        shader.uploadFloat("uTime", Time.getTime());

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

        shader.detach();
    }

}

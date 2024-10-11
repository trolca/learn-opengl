package me.trolca.renderer;

import me.trolca.jade.Window;
import me.trolca.jade.assetspools.ShaderPool;
import me.trolca.jade.components.SpriteRenderer;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static me.trolca.jade.components.SpriteRenderer.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    private final int VERTICES_SQUARE_SIZE;
    private final int MAX_BATCH_SIZE;
    private final float[] verticesArray;
    private final int[] elementArray;
    private final SpriteRenderer[] spriteRenderers;
    private int nextFree;
    private boolean isFree;

    private Shader shader;
    private int vaoID, vboID, eboID;

    public RenderBatch(int maxBatchSize){
        this.shader = ShaderPool.DEFAULT_SHADER;
        this.MAX_BATCH_SIZE = maxBatchSize;

        this.VERTICES_SQUARE_SIZE = SpriteRenderer.PROPERTIES_SIZE * 4;
        this.verticesArray = new float[this.VERTICES_SQUARE_SIZE * MAX_BATCH_SIZE];
        this.elementArray = new int[MAX_BATCH_SIZE * 6];
        this.spriteRenderers = new SpriteRenderer[MAX_BATCH_SIZE];

        this.nextFree = 0;
        this.isFree = true;
        generateElements();
        init();
    }

    public void init(){
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) verticesArray.length * Float.BYTES, GL_DYNAMIC_DRAW);


        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementArray, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, SpriteRenderer.PROPERTIES_SIZE_BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, SpriteRenderer.PROPERTIES_SIZE_BYTES, POS_SIZE * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, SpriteRenderer.PROPERTIES_SIZE_BYTES, (POS_SIZE + COLOR_SIZE) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    public void render(){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, verticesArray);

        shader.use();

        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, nextFree * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);

        shader.detach();
    }

    public void addSpriteRenderer(SpriteRenderer spriteRenderer){
        if(!isFree) return;
        spriteRenderers[nextFree] = spriteRenderer;
        float[] verticesToAdd = spriteRenderer.getVertices();
        System.arraycopy(verticesToAdd, 0, verticesArray, nextFree * VERTICES_SQUARE_SIZE, verticesToAdd.length);

        if(nextFree > 9950) System.out.println(Arrays.toString(verticesArray));

        nextFree++;
        if(nextFree >= MAX_BATCH_SIZE) isFree = false;
    }

    private void generateElements(){
        for(int i=0; i < elementArray.length; i++){
            int dividedValue = i / 6;
            int addVertexIndex = dividedValue * 4;

            elementArray[i] = SpriteRenderer.ELEMENT_TEMPLATE[i % 6] + addVertexIndex;
        }

    }


    public boolean isFree(){
        return isFree;
    }

}

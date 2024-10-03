package me.trolca.renderer;

import me.trolca.jade.components.SpriteRenderer;
import me.trolca.utils.Utils;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    private final int MAX_BATCH_SIZE = 5;
    private final float[] vertexBuffer = new float[SpriteRenderer.PROPERTIES_SIZE * 4 * MAX_BATCH_SIZE];
    private final SpriteRenderer[] spriteRenderers = new SpriteRenderer[MAX_BATCH_SIZE];
    private int nextFree;
    private boolean isFree;

    private int vaoID, vboID, eboID;

    public RenderBatch(){
        this.nextFree = 0;
        this.isFree = true;
    }

    public void init(){
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

    }

    public void render(){

    }

    public void addSpriteRenderer(SpriteRenderer spriteRenderer){
        if(!isFree) return;
        spriteRenderers[nextFree] = spriteRenderer;
        float[] verticesToAdd = spriteRenderer.getVertices();
        System.arraycopy(verticesToAdd, 0, vertexBuffer, nextFree * SpriteRenderer.PROPERTIES_SIZE, verticesToAdd.length);

        nextFree++;
        if(nextFree >= MAX_BATCH_SIZE) isFree = false;
    }

}

package me.trolca.renderer;

import me.trolca.jade.components.SpriteRenderer;
import me.trolca.utils.Utils;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    private final int MAX_BATCH_SIZE = 5;
    private final float[] vertexBuffer = new float[SpriteRenderer.PROPERTIES_SIZE * 4 * MAX_BATCH_SIZE];
    private final int[] elementBuffer = new int[MAX_BATCH_SIZE * 3];
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

        int posSize = 2;
        int colorSize = 4;
        int uvSize = 2;

        glVertexAttribPointer(0, posSize,  GL_FLOAT, false, SpriteRenderer.PROPERTIES_SIZE, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize,  GL_FLOAT, false, SpriteRenderer.PROPERTIES_SIZE, posSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize,  GL_FLOAT, false, SpriteRenderer.PROPERTIES_SIZE, (posSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
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

    private void addElements(int thisFree){
        //TODO: better this :>
        int nextIndex = thisFree * 4;
        elementBuffer[thisFree] = nextIndex + 1;

    }

}

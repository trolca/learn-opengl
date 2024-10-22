package me.trolca.renderer;

import me.trolca.jade.Transform;
import me.trolca.jade.Window;
import me.trolca.jade.assetspools.ShaderPool;
import me.trolca.jade.components.SpriteRenderer;
import me.trolca.utils.Utils;
import org.joml.Vector2f;

import java.awt.*;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    public static final int PROPERTIES_SIZE = 9;
    public static final int PROPERTIES_SIZE_BYTES = PROPERTIES_SIZE * Float.BYTES;

    public static final int POS_SIZE = 2;
    public static final int COLOR_SIZE = 4;
    public static final int UV_SIZE = 2;
    public static final int TEX_ID_SIZE = 1;

    public static final int[] ELEMENT_TEMPLATE = {2,0,1,3,2,1};

    private final int TEXTURES_BUFFER_SIZE;
    private final int VERTICES_SQUARE_SIZE;
    private final int MAX_BATCH_SIZE;
    private final float[] verticesArray;
    private final int[] elementArray;
    private final SpriteRenderer[] spriteRenderers;
    private final Texture[] activeTextures;
    private final int[] availableSlots;
    private int nextFree;
    private boolean isFree;

    private Shader shader;
    private int vaoID, vboID, eboID;

    public RenderBatch(int maxBatchSize){
        this.shader = ShaderPool.DEFAULT_SHADER;
        this.MAX_BATCH_SIZE = maxBatchSize;
        this.TEXTURES_BUFFER_SIZE = 16;
        this.activeTextures = new Texture[this.TEXTURES_BUFFER_SIZE];
        this.availableSlots = new int[this.TEXTURES_BUFFER_SIZE];

        for(int i=0; i < TEXTURES_BUFFER_SIZE; i++){
            availableSlots[i] = i;
        }

        this.VERTICES_SQUARE_SIZE = PROPERTIES_SIZE * 4;
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


        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, PROPERTIES_SIZE_BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, PROPERTIES_SIZE_BYTES, POS_SIZE * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, PROPERTIES_SIZE_BYTES, (POS_SIZE + COLOR_SIZE) * Float.BYTES);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, PROPERTIES_SIZE_BYTES, (POS_SIZE + COLOR_SIZE + UV_SIZE) * Float.BYTES);
        glEnableVertexAttribArray(3);

    }

    public void render(){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, verticesArray);

        shader.use();

        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());
        for(int i=0; i < activeTextures.length; i++){
            if(activeTextures[i] != null) {
                glActiveTexture(GL_TEXTURE0 + i);
                activeTextures[i].bind();
            }
        }
        shader.uploadTextureArray("TEX_SAMPLER", availableSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, nextFree * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        glBindVertexArray(0);

        shader.detach();
    }

    /**
     * Adds the vertices for this sprite.
     * The vertices will always create a rectangle where the vertices are:
     * <ol>
     *     <li>Bottom left</li>
     *     <li>Bottom right</li>
     *     <li>Top left</li>
     *     <li>Top right</li>
     * </ol>
     * <br>
     * The first 2 floats is the position of the sprite. <br>
     * Another 4 is the color with the position: rgba <br>
     * The last 2 ones are the uv texture coordinates.
     */
    public void addSpriteRenderer(SpriteRenderer spriteRenderer){
        if(!isFree) return;
        spriteRenderers[nextFree] = spriteRenderer;
        Texture texture = spriteRenderer.getTexture();
        Transform transform = spriteRenderer.gameObject.transform;

        // texId = -1 not enough space for batching or no texture
        int texId;
        if(texture != null){
            texId = Utils.getIndexOf(activeTextures, texture);
            if(texId == -1)
                texId = addTexture(texture);
        }else{
            texId = -1;
        }

        float[] vertices = new float[PROPERTIES_SIZE * 4];

        generatePositionData(vertices, transform);
        generateColorData(vertices, spriteRenderer.getColor());
        generateUvData(vertices);
        generateTexId(vertices, texId);

        System.arraycopy(vertices, 0, verticesArray, nextFree * VERTICES_SQUARE_SIZE, vertices.length);

        System.out.println(Arrays.toString(verticesArray));

        nextFree++;
        if(nextFree >= MAX_BATCH_SIZE) isFree = false;
    }

    private void generateElements(){

        for(int i=0; i < elementArray.length; i++){
            int dividedValue = i / 6;
            int addVertexIndex = dividedValue * 4;

            elementArray[i] = ELEMENT_TEMPLATE[i % 6] + addVertexIndex;
        }
    }

    private int addTexture(Texture texture){

        for(int i=0; i < activeTextures.length; i++){
            if(activeTextures[i] == null){
                activeTextures[i] = texture;
                return i;
            }

        }
        return -1;
    }


    private void generateTexId(float[] vertices, int texId){
        for(int i=0; i < 4; i++){
            int iter = PROPERTIES_SIZE * i;
            vertices[8 + iter] = texId;
        }
    }

    private void generateUvData(float[] vertices){
        float x;
        float y;

        for(int i=0; i < 4; i++){
            x = 0;
            y = 0;
            switch (i){
                case 0 -> y = 1;
                case 1 -> {x = 1; y = 1;}
                case 3 -> x = 1;
            }
            int iter = PROPERTIES_SIZE * i;
            vertices[6 + iter] = x;
            vertices[7 + iter] = y;
        }
    }

    private void generateColorData(float[] vertices , Color color){

        int r, g, b;
        float a;


        if(color != null){
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();
            a = color.getAlpha()/255.0f;
        }else{
            r = 0;
            g = 0;
            b = 0;
            a = 0;
        }

        for(int i=0; i < 4; i++){
            int iter = PROPERTIES_SIZE * i;
            vertices[2 + iter] = r;
            vertices[3 + iter] = g;
            vertices[4 + iter] = b;
            vertices[5 + iter] = a;
        }

    }

    private void generatePositionData(float[] vertices, Transform transform){
        Vector2f position = transform.position;
        Vector2f scale = transform.scale;

        float xPos;
        float yPos;

        for(int i=0; i < 4; i++){
            xPos = position.x;
            yPos = position.y;

            if(i == 1){
                xPos += scale.x;
            }else if(i == 2){
                yPos += scale.y;
            }else if(i == 3){
                xPos += scale.x;
                yPos += scale.y;
            }
            int iter = PROPERTIES_SIZE * i;
            vertices[iter] = xPos;
            vertices[1 + iter] = yPos;
        }

    }


    public boolean isFree(){
        return isFree;
    }

}

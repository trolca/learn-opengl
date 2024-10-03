package me.trolca.jade.components;

import me.trolca.jade.Component;
import me.trolca.jade.Transform;
import me.trolca.renderer.Texture;
import org.joml.Vector2f;

import java.awt.*;
import java.util.Arrays;

public class SpriteRenderer extends Component {

    public static final int PROPERTIES_SIZE = 8;
    public static final int PROPERTIES_SIZE_BYTES = PROPERTIES_SIZE * Float.BYTES;

    public static final int POS_SIZE = 2;
    public static final int COLOR_SIZE = 4;
    public static final int UV_SIZE = 2;

    public static final int[] ELEMENT_TEMPLATE = {2,0,1,3,2,1};

    private Color color;
    private Texture texture;

    public SpriteRenderer(){
        this(new Texture("assets/textures/testImage.png"));
    }

    public SpriteRenderer(Color color){
        this(color, new Texture("assets/textures/testImage.png"));
    }

    public SpriteRenderer(Texture texture){
        this(null, texture);
    }

    public SpriteRenderer(Color color, Texture texture){
        this.texture = texture;
        this.color = color;
    }


    /**
     * Gets the vertices to create this sprite.
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
     * @return The array of vertices for this sprite. The size is always 4.
     */
    public float[] getVertices(){
        Transform transform = this.gameObject.transform;

        float[] vertices = new float[PROPERTIES_SIZE * 4];

        generatePositionData(vertices, transform);
        generateColorData(vertices, color);
        generateUvData(vertices);

        return vertices;
    }

    @Override
    public void start(){

    }

    @Override
    public void update(float dt) {
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
            vertices[6 + iter] = 1.0f;
            vertices[7 + iter] = 1.0f;
        }
    }

    private void generateColorData(float[] vertices ,Color color){

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
}

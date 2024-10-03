package me.trolca.jade.components;

import me.trolca.jade.Component;
import me.trolca.jade.Transform;
import me.trolca.renderer.Texture;
import org.joml.Vector2f;

import java.awt.*;

public class SpriteRenderer extends Component {

    public static final int PROPERTIES_SIZE = 8;

    private Color color;
    private Texture texture;

    public SpriteRenderer(){
        this(new Texture("assets/textures/testImage.png"));
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

        float[] colorFloat = {color.getGreen(), color.getBlue(), color.getAlpha()};
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
            vertices[6 + iter] = x;
            vertices[7 + iter] = y;
        }
    }

    private void generateColorData(float[] vertices ,Color color){

        int r, g, b, a;

        if(color != null){
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();
            a = color.getAlpha();
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
                yPos = scale.y;
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

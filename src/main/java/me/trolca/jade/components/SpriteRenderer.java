package me.trolca.jade.components;

import me.trolca.jade.Component;
import me.trolca.jade.Transform;
import me.trolca.renderer.Texture;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.Arrays;

public class SpriteRenderer extends Component {

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

    @Override
    public void start(){

    }

    @Override
    public void update(float dt) {
    }

    @Nullable
    public Texture getTexture(){
        return texture;
    }

    public Color getColor(){
        return color;
    }
}

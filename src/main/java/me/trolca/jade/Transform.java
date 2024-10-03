package me.trolca.jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Transform  {

    public Vector2f position;
    public Vector2f scale;

    public Transform(){
        this(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale){
        this.position = position;
        this.scale = scale;
    }

}

package me.trolca.jade.scenes;

import me.trolca.jade.Camera;

public abstract class Scene {

    protected Camera camera;

    public Scene(){
    }

    protected void init(){

    }

    public abstract void update(float dt);
}

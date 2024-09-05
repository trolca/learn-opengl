package me.trolca.jade.scenes;

public abstract class Scene {

    public Scene(){
        init();
    }

    protected abstract void init();

    public abstract void update(float dt);
}

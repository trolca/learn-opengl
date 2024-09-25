package me.trolca.jade.scenes;

import me.trolca.jade.Camera;
import me.trolca.jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    private boolean isRunning = false;

    public Scene(){
    }

    public void init(){

    }

    public void start(){
        for(GameObject gameObject : gameObjects){
            gameObject.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject){
        if(!isRunning){
            gameObjects.add(gameObject);
        }else{
            gameObjects.add(gameObject);
            gameObject.start();
        }
    }

    public abstract void update(float dt);
}

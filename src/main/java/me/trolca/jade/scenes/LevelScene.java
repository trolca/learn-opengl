package me.trolca.jade.scenes;

import me.trolca.jade.*;
import me.trolca.jade.Window;
import me.trolca.jade.components.SpriteRenderer;
import me.trolca.renderer.Renderer;
import me.trolca.utils.ShaderUtils;
import org.joml.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class LevelScene extends Scene{

    private String[] shaders;

    public LevelScene(){
        try {
            this.shaders = ShaderUtils.getShaders("default.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void init() {
        this.addGameObjectToScene(new GameObject("Super testes", new Transform(new Vector2f(100f, 100f), new Vector2f(100.0f, 100.0f)))
                .addComponent(new SpriteRenderer(Color.BLACK)));

//        for(int i=0; i < 10_000; i++){
//            GameObject go = new GameObject("Render test " + i, new Transform(i+30, i+30, 27.0f, 27.0f))
//                    .addComponent(new SpriteRenderer(Color.BLACK));
//
//            this.addGameObjectToScene(go);
//        }

        this.camera = new Camera(new Vector2f(0.0f,0.0f));
        this.renderer = new Renderer();

    }

    @Override
    public void update(float dt) {

        if(KeyListener.isKeyPressed(KeyEvent.VK_W)){
            camera.position.add(0,  dt*500.0f);
        }else if(KeyListener.isKeyPressed(KeyEvent.VK_S)){
            camera.position.sub(0, dt*500.0f);
        }else if(KeyListener.isKeyPressed(KeyEvent.VK_A)){
            camera.position.sub(dt*500, 0);
        }else if(KeyListener.isKeyPressed(KeyEvent.VK_D)){
            camera.position.add(dt*500, 0);
        }

        this.getRenderer().render();

    }


}

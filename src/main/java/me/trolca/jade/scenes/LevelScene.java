package me.trolca.jade.scenes;

import me.trolca.utils.ShaderUtils;

import java.io.IOException;

public class LevelScene extends Scene{

    private String[] shaders;

    public LevelScene(){
        try {
            this.shaders = ShaderUtils.getShaders("default.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Inside level scene");
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

    }


}

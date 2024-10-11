package me.trolca.utils;

import me.trolca.renderer.Shader;

public class Pools {
    public static final AssetPool<Shader> SHADERS = AssetPool.createNew(Shader.class);

    public static void register(){
        System.out.println("Registered all of the asset pools!");
    }

}

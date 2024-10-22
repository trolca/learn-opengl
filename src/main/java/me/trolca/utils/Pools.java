package me.trolca.utils;

import me.trolca.renderer.Shader;
import me.trolca.renderer.Texture;

public class Pools {
    public static final AssetPool<Shader> SHADERS = AssetPool.createNew(Shader.class);
    public static final AssetPool<Texture> TEXTURES = AssetPool.createNew(Texture.class);

    public static void register(){
        System.out.println("Registered all of the asset pools!");
    }

}

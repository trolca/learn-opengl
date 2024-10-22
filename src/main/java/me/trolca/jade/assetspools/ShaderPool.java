package me.trolca.jade.assetspools;

import me.trolca.renderer.Shader;
import me.trolca.utils.AssetPool;
import me.trolca.utils.Pools;

public class ShaderPool {

    public static final Shader DEFAULT_SHADER = register(new Shader("assets/shaders/default.glsl"));

    private static Shader register(Shader shader){
        AssetPool.register(Pools.SHADERS,  shader);
        return shader;
    }

    public static void register(){
        System.out.println("Registered all of the asset pools!");
    }
}

package me.trolca.jade.assetspools;

import me.trolca.renderer.Texture;
import me.trolca.utils.AssetPool;
import me.trolca.utils.Pools;

public class TexturePool {

    public static final Texture MARIO_TEXTURE = register(new Texture("assets/textures/testImage.png"));
    public static final Texture TROLCA_TEXTURE = register(new Texture("assets/textures/trolca.png"));

    private static Texture register(Texture texture){
        AssetPool.register(Pools.TEXTURES,  texture);
        return texture;
    }

    public static void register(){
        System.out.println("Registered all of the asset pools!");
    }
}

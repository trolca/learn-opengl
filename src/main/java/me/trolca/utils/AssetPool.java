package me.trolca.utils;

import me.trolca.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AssetPool<T> {
    private final List<T> assets = new ArrayList<>();

    public void register(T asset){
        assets.add(asset);
    }

    public static <R> R register(AssetPool<R> assetPool, R asset){
        return asset;
    }

    public static <R> AssetPool<R> createNew(Class<R> createTo){
        if(Main.isRunning()) throw new IllegalStateException("Cannot create new pools after starting game!");
        return new AssetPool<>() {
            @Override
            public void register(R asset) {
                super.register(asset);
            }
        };
    }


}

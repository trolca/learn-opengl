package me.trolca;

import me.trolca.jade.Window;
import me.trolca.jade.assetspools.ShaderPool;
import me.trolca.jade.components.SpriteRenderer;
import me.trolca.utils.Pools;

public class Main {

    private static boolean IS_RUNNING = false;

    public static void main(String[] args) {

        Window mainWindow = Window.get();
        mainWindow.run();

        IS_RUNNING = true;
    }

    public static boolean isRunning(){
        return IS_RUNNING;
    }
}
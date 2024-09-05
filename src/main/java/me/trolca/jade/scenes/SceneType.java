package me.trolca.jade.scenes;


import java.lang.reflect.InvocationTargetException;

public enum SceneType {

    LEVEL_EDITOR(LevelEditorScene.class),
    LEVEL_PLAY(LevelScene.class);

    private final Class<? extends Scene> sceneClass;

    SceneType(Class<? extends Scene> clazz){
        this.sceneClass = clazz;
    }

    public Scene getInstance(){
        try {
            return sceneClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

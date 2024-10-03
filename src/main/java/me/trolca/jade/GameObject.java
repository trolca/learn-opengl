package me.trolca.jade;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    private List<Component> components;
    public Transform transform;

    public GameObject(String name){
        this(name, new Transform());
    }

    public GameObject(String name, Transform transform){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                return componentClass.cast(c);
            }

        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){

        for(int i=0; i < components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public GameObject addComponent(Component c){
        this.components.add(c);
        c.gameObject = this;
        return this;
    }

    public void update(float dt){
        for(Component c : components){
            c.update(dt);
        }
    }

    public void start(){
        for(Component c : components){
            c.start();
        }
    }

}

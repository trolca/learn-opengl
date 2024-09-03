package me.trolca.jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener INSTANCE;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollY = 0d;
        this.scrollY = 0d;
        this.xPos = 0d;
        this.yPos = 0d;
        this.lastY = 0d;
        this.lastX = 0d;
        this.isDragging = false;
    }

    public static MouseListener get(){
        if(INSTANCE == null){
            MouseListener.INSTANCE = new MouseListener();
        }

        return INSTANCE;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        MouseListener mouseListener = get();

        mouseListener.lastX = mouseListener.xPos;
        mouseListener.lastY = mouseListener.yPos;
        mouseListener.xPos = xpos;
        mouseListener.yPos = ypos;

        if(mouseListener.lastX == mouseListener.xPos &&
        mouseListener.lastY == mouseListener.yPos) return;

        for(boolean pressed : mouseListener.mouseButtonPressed){

            if(pressed){
                mouseListener.isDragging = true;
                break;
            }

        }

    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        MouseListener mouseListener = get();
        if(button >= mouseListener.mouseButtonPressed.length) return;

        if(action == GLFW_PRESS) {
            mouseListener.mouseButtonPressed[button] = true;
        }else if (action == GLFW_RELEASE){
            mouseListener.mouseButtonPressed[button] = false;
            mouseListener.isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        MouseListener listener = get();

        listener.scrollX = xOffset;
        listener.scrollY = yOffset;
    }

    public static void endFrame(){
        MouseListener listener = get();

        listener.scrollX = 0;
        listener.scrollY = 0;
        listener.lastX = listener.xPos;
        listener.lastY = listener.yPos;
    }

    public static float getX(){
        return (float) get().xPos;
    }

    public static float getY(){
        return (float) get().yPos;
    }

    public static float getDx(){
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy(){
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isAnyKeyPressed(){
        boolean[] keyPressed = get().mouseButtonPressed;

        for(boolean pressed : keyPressed){

            if(pressed) return true;

        }

        return false;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button >= get().mouseButtonPressed.length) return false;
        return get().mouseButtonPressed[button];
    }

}

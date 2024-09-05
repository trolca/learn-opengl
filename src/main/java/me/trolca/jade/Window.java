package me.trolca.jade;

import me.trolca.jade.scenes.Scene;
import me.trolca.jade.scenes.SceneType;
import me.trolca.jade.utils.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;

    public float r, g, b, a;

    private GLFWCursorPosCallback mouseMoveCallback = null;
    private GLFWMouseButtonCallback mouseButtonCallback = null;
    private GLFWScrollCallback mouseScrollCallback = null;
    private GLFWKeyCallback keyCallback = null;

    private static Window window = null;

    private static Scene currentScene = null;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Sussy baka";
    }

    public static void changeScene(SceneType sceneType){
        currentScene = sceneType.getInstance();
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run(){
        System.out.println("HELLO LWJGL " + Version.getVersion());

        init();
        loop();

        //Free the memory
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

        mouseMoveCallback.close();
        mouseButtonCallback.close();
        mouseScrollCallback.close();
        keyCallback.close();
    }

    private void init(){
        //Error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initalizes GLFW

        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Couldn't initialize ");
        }

        //Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        //Create the window
        glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if(glfwWindow == NULL){
            throw new IllegalStateException("Couldn't create the window!");
        }

        mouseMoveCallback = glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        mouseButtonCallback = glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        mouseScrollCallback = glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        keyCallback = glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        //Make the OpenGL context current

        GLFW.glfwMakeContextCurrent(glfwWindow);

        GLFW.glfwSwapInterval(1);

        //Make the window visible

        GLFW.glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        Window.changeScene(SceneType.LEVEL_EDITOR);
    }

    private void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        r = 1.0f;
        g = 1.0f;
        b = 1.0f;
        a = 1.0f;

        while (!GLFW.glfwWindowShouldClose(glfwWindow)){
            //Poll events (inputs)
            GLFW.glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0) {
                currentScene.update(dt);
            }
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;

            System.out.println((1.0f / dt) + "FPS");
        }
    }

}

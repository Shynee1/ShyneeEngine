package gameEngine;

import gameEngine.abstracts.Scene;
import gameEngine.listeners.KeyListener;
import gameEngine.listeners.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import scenes.LevelEditorScene;
import scenes.LevelScene;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    /*
     Creates a simple LWJGL window as a singleton. Use create() to initialize variables and start the loop.
    */

    private static Window window = null;
    private static Scene currentScene;
    private ImGuiLayer imGuiLayer;

    private int width;
    private int height;
    private String title;
    public float r, g, b, a;

    //Pointer to window in memory
    private long glfwWindow;

    private Window(){
        //Initialize variables with placeholders
        this.title = "Window";
        this.width = 0;
        this.height = 0;
        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0 -> {
                currentScene = new LevelEditorScene();
            }
            case 1 -> {
                currentScene = new LevelScene();
            }
            default -> {
                assert false : "Unknown scene '" + newScene + "'";
            }
        }
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (window == null){
            window = new Window();
        }
        return window;
    }

    public void create(String gameTitle, int width, int height){
        this.title = gameTitle;
        this.width = width;
        this.height = height;

        run();
    }

    public void run(){
        System.out.println("Version: " + Version.getVersion());

        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(){

        //Create error callback to System build log
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize and configure GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Could not initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //Create the window and store memory space
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL){
            throw new IllegalStateException("Could not create window");
        }

        //Add mouse/keyboard callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        //Enable OpenGL
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync/buffer-swapping
        glfwSwapInterval(1);
        //Set window to visible
        glfwShowWindow(glfwWindow);

        //Utilize GLFW and OpenGL properly (REQUIRED)
        GL.createCapabilities();

        //Enable alpha blending with simple function
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        //Use imGui
        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        Window.changeScene(0);
    }
    private void loop(){

        float startTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        currentScene.load();
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt>=0) currentScene.update(dt);

            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime-startTime;
            startTime = endTime;
        }
        currentScene.saveExit();
    }

    public static Scene getScene(){
        return currentScene;
    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width = newWidth;
    }

    public static void setHeight(int newHeight){
        get().height = newHeight;
    }
}

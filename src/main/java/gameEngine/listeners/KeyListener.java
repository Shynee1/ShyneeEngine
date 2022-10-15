package gameEngine.listeners;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    /*
     Key listener singleton with GLFW key callback
    */

    private static KeyListener instance;
    private boolean[] keyPressed = new boolean[GLFW_KEY_LAST];
    private boolean keyDown;

    private KeyListener(){
        this.keyDown = false;
    }

    public static KeyListener get(){
        if (instance == null){
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
            get().keyDown = true;
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
            get().keyDown = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
        return get().keyPressed[keyCode];
    }

    public static boolean isAnyKeyPressed(){return get().keyDown;}
}

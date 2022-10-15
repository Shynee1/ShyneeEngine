package gameEngine.listeners;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    /*
     Mouse listener singleton with GLFW position and scroll callbacks
    */

    private static MouseListener instance;
    private double scrollX, scrollY;
    private double x, y, lastX, lastY;
    private final boolean[] mouseButtonPressed = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private boolean isButtonPressed;
    private boolean isDragging;

    private MouseListener(){
        //Initialize to avoid weird bug
        this.scrollX = 0;
        this.scrollY = 0;
        this.x = 0;
        this.y = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.isButtonPressed = false;
    }

    public static MouseListener get(){
        if (instance == null){
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePositionCallback(long window, double x, double y){
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = x;
        get().y = y;
        get().isDragging = get().isButtonPressed;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (action == GLFW_PRESS){
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
                get().isButtonPressed = true;
            }
        } else if (action == GLFW_RELEASE){
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isButtonPressed = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().x;
        get().lastY = get().y;
    }

    public static float getX(){
        return (float) get().x;
    }

    public static float getY(){
        return (float) get().y;
    }

    public static float getDX(){
        return (float) (get().lastX - get().x);
    }

    public static float getDY(){
        return (float) (get().lastY - get().y);
    }

    public static float getScrollX(){
        return (float) (get().scrollX);
    }

    public static float getScrollY(){
        return (float) (get().scrollY);
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }
        else{
            return false;
        }
    }

    public static boolean isAnyButtonDown(){
        return get().isButtonPressed;
    }

}

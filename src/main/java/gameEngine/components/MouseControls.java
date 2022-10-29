package gameEngine.components;

import gameEngine.Window;
import gameEngine.abstracts.Component;
import gameEngine.listeners.MouseListener;
import gameEngine.objects.GameObject;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickup(GameObject go){
        holdingObject = go;
        Window.getScene().addGameObject(go);
    }

    public void place(){
        holdingObject = null;
    }

    @Override
    public void update(float dt){
        if (holdingObject != null){
            holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) place();
        }
    }
}

package gameEngine.components;

import gameEngine.Window;
import gameEngine.abstracts.Component;
import gameEngine.listeners.MouseListener;
import gameEngine.objects.GameObject;
import org.joml.Vector2f;
import util.Constants;

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

            Vector2f pos = holdingObject.transform.position;

            pos.x = MouseListener.getOrthoX();
            pos.y = MouseListener.getOrthoY();
            pos.x = (int) (pos.x / Constants.GRID_WIDTH) * Constants.GRID_WIDTH;
            pos.y = (int) (pos.y/Constants.GRID_HEIGHT) * Constants.GRID_HEIGHT;

            holdingObject.transform.position.x = pos.x;
            holdingObject.transform.position.y = pos.y;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) place();
        }
    }
}

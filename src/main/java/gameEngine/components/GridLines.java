package gameEngine.components;

import gameEngine.Window;
import gameEngine.abstracts.Component;
import gameEngine.objects.Camera;
import gameEngine.renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Constants;

public class GridLines extends Component {

    @Override
    public void update(float dt){
        Camera sceneCamera = Window.getScene().getCamera();

        Vector2f cameraPos = sceneCamera.position;
        Vector2f projectionSize = sceneCamera.getProjectionSize();

        int width = (int) projectionSize.x;
        int height = (int) projectionSize.y;

        int firstX = (int)(cameraPos.x/Constants.GRID_WIDTH) * Constants.GRID_WIDTH;
        int firstY = (int) (cameraPos.y/Constants.GRID_HEIGHT) * Constants.GRID_HEIGHT;

        int vLines = width/Constants.GRID_WIDTH;
        int hLines = height/Constants.GRID_HEIGHT;

        int maxLines = Math.max(vLines, hLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i = 0; i < maxLines; i++){
            int x = firstX + (Constants.GRID_WIDTH * i);
            int y = firstY + (Constants.GRID_HEIGHT* i);

            if (i < vLines) DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, y + height), color);
            if (i < hLines) DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(x + width, y), color);
        }
    }
}

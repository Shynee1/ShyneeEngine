package gameEngine.objects;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Constants;

public class Camera {
    private Matrix4f projMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f cameraPos) {
        this.position = cameraPos;
        this.projMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        this.projMatrix.identity();

        this.projMatrix.ortho(0f, Constants.ORTHO_SCREEN_RIGHT, 0f, Constants.ORTHO_SCREEN_TOP, 0f, 100f);
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0f, 0f, -1f);
        Vector3f cameraUp = new Vector3f(0f, 1f, 0f);

        this.viewMatrix.identity();
        this.viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20f),
                cameraFront.add(new Vector3f(position.x, position.y, 0f)),
                cameraUp);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projMatrix;
    }
}

package gameEngine.abstracts;

import gameEngine.objects.Camera;
import gameEngine.objects.GameObject;
import gameEngine.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected Renderer renderer = new Renderer();
    protected boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    }

    public void init(){

    }

    public void start(){
        for (GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObject(GameObject go){
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera getCamera(){
        return this.camera;
    }
}
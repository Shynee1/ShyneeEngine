package gameEngine.abstracts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameEngine.objects.Camera;
import gameEngine.objects.GameObject;
import gameEngine.renderer.Renderer;
import gameEngine.serialization.ComponentSerialization;
import gameEngine.serialization.GameObjectSerialization;
import imgui.ImGui;
import util.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected Renderer renderer = new Renderer();
    protected boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

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

    public void sceneImgui(){
        if (activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    /**
     * Saves all GameObjects in Scene to json file
     */
    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerialization())
                .registerTypeAdapter(GameObject.class, new GameObjectSerialization())
                .create();

        try {
            FileWriter writer = new FileWriter("level.json");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads GameObjects from json file
     */
    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerialization())
                .registerTypeAdapter(GameObject.class, new GameObjectSerialization())
                .create();

        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get(Constants.saveFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")){
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (GameObject go : objs){
                this.addGameObject(go);
            }

            this.levelLoaded = true;
        }
    }

    public void imgui(){

    }
}

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

import java.io.File;
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

    /**
     * Creates ImGui window for each scene. Calls on GameObject to fill window
     */
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
            FileWriter writer = new FileWriter(Constants.saveFileName, false);
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

        File file = new File(Constants.saveFileName);
        String inFile = "";

        if (!file.exists()) return;

        try {
            inFile = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!inFile.equals("")){
            int maxGoId = -1;
            int maxCompId = -1;

            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (GameObject go : objs){
                this.addGameObject(go);

                for (Component c : go.getAllComponents()){
                    if (c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }

                if (go.getUid() > maxGoId){
                    maxGoId = go.getUid();
                }
            }

            //Sets max id counter to one higher than value loaded from save file
            GameObject.init(++maxGoId);
            Component.init(++maxCompId);

            this.levelLoaded = true;
        }
    }

    public void imgui(){

    }
}

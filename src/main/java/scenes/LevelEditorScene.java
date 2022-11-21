package scenes;

import gameEngine.ImGuiLayer;
import gameEngine.Transform;
import gameEngine.abstracts.Scene;
import gameEngine.components.GridLines;
import gameEngine.components.MouseControls;
import gameEngine.objects.Camera;
import gameEngine.objects.GameObject;
import gameEngine.renderer.DebugDraw;
import gameEngine.sprites.Sprite;
import gameEngine.sprites.Spritesheet;
import gameEngine.util.AssetPool;
import gameEngine.util.Prefabs;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private GameObject obj2;
    private GameObject obj3;
    private Spritesheet spritesheet;

    //Empty game object
    private final GameObject levelEditorComponents = new GameObject("LEComponents", new Transform(), 0);

    @Override
    public void init() {
        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new GridLines());

        loadResources();
        spritesheet = AssetPool.getSpritesheet("assets/spritesheets/blocks.png");
        this.camera = new Camera(new Vector2f(0, 0));

        if (levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        /*
        obj1 = new GameObject("obj1", new Transform(new Vector2f(0, 100), new Vector2f(256, 256)), 0);
        obj1.addComponent(new SpriteRenderer().setColor(new Vector4f(1, 0, 0, 1)));
        obj1.addComponent(new Rigidbody());
        this.addGameObject(obj1);

        obj2 = new GameObject("obj2", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        obj2.addComponent(new SpriteRenderer().setTexture(AssetPool.getTexture("assets/testImages/blendImage2.png")));
        this.addGameObject(obj2);

         */
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        //TODO: FIX TEXTURE LOADING BUG
        AssetPool.addSpritesheet("assets/spritesheets/blocks.png",
                new Spritesheet(AssetPool.getTexture("assets/spritesheets/blocks.png"),
                        16, 16, 81, 0));

        AssetPool.getTexture("assets/testImages/blendImage1.png");
        AssetPool.getTexture("assets/testImages/blendImage2.png");
    }

    int rotation = 0;
    @Override
    public void update(float dt) {

        DebugDraw.addBox2D(new Vector2f(300, 200), new Vector2f(300, 100), rotation++, new Vector3f(1, 0, 0), 1);

        levelEditorComponents.update(dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.pushFont(ImGuiLayer.fonts.get("assets/fonts/ARLRDBD.TTF"));
        ImGui.begin("Block Picker");

        ImVec2 windowPos = new ImVec2();
        ImVec2 windowSize = new ImVec2();
        ImVec2 itemSpacing = new ImVec2();

        ImGui.getWindowPos(windowPos);
        ImGui.getWindowSize(windowSize);
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x+ windowSize.x;
        for (int i = 0; i < spritesheet.size(); i++){
            Sprite sprite = spritesheet.getSprite(i);
            float sWidth = sprite.getWidth()*2;
            float sHeight = sprite.getHeight()*2;
            int sId = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(sId, sWidth, sHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObject obj = Prefabs.generateSpriteObject(sprite, 32, 32);
                //Attach to the mouse cursor
                levelEditorComponents.getComponent(MouseControls.class).pickup(obj);
            }
            ImGui.popID();

            //Place buttons on same line if they do not go over window width
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2+ itemSpacing.x + sWidth;
            if (i + 1 < spritesheet.size() && nextButtonX2<=windowX2)
                ImGui.sameLine();
        }

        ImGui.end();
        ImGui.popFont();
    }
}

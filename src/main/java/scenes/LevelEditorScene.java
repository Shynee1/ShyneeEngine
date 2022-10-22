package scenes;

import gameEngine.Transform;
import gameEngine.abstracts.Scene;
import gameEngine.components.SpriteRenderer;
import gameEngine.objects.Camera;
import gameEngine.objects.GameObject;
import gameEngine.sprites.Spritesheet;
import gameEngine.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private GameObject obj2;
    private Spritesheet spritesheet;

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        if (levelLoaded) return;

        spritesheet = AssetPool.getSpriteSheet("assets/spritesheets/spritesheet.png");

        obj1 = new GameObject("obj1", new Transform(new Vector2f(0, 100), new Vector2f(256, 256)), 1);
        obj1.addComponent(new SpriteRenderer().setColor(new Vector4f(1, 0, 0, 1)));
        this.addGameObject(obj1);
        this.activeGameObject = obj1;

        obj2 = new GameObject("obj2", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), -1);
        obj2.addComponent(new SpriteRenderer().setTexture(AssetPool.getTexture("assets/testImages/blendImage2.png")));
        this.addGameObject(obj2);

    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/spritesheets/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/spritesheets/spritesheet.png"),
                        16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}

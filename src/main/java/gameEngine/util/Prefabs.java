package gameEngine.util;

import gameEngine.Transform;
import gameEngine.components.SpriteRenderer;
import gameEngine.objects.GameObject;
import gameEngine.sprites.Sprite;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = new GameObject("SpriteGenObject", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        block.addComponent(new SpriteRenderer().setSprite(sprite));

        return block;
    }
}

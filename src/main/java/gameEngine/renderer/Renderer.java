package gameEngine.renderer;

import gameEngine.components.SpriteRenderer;
import gameEngine.objects.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Renders a collection of RenderBatches
 */
public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    /**
     * Gets a sprite and adds it to the batch
     * @param go The GameObject that contains the sprite
     */
    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    /**
     * Adds sprite to appropriate RenderBatch or creates a new RenderBatch
     * @param sprite The sprite to be added to the batch
     */
    private void add(SpriteRenderer sprite) {
        boolean added = false;

        for (RenderBatch batch : batches) {
            //Only store sprites with the same zIndex
            if (batch.hasRoom() && batch.getZIndex() == sprite.gameObject.getZIndex()) {
                Texture texture = sprite.getTexture();
                if (texture != null && (batch.hasTexture(texture) || batch.hasTextureRoom())){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        //Create new RenderBatch if sprite was not added to an already existing batch
        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            //Sort batches list based on zIndex
            Collections.sort(batches);
        }
    }

    /**
     * Render every RenderBatch
     */
    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
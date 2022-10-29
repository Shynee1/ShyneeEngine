package gameEngine.sprites;

import gameEngine.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {

    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCoords = new Vector2f[]{
        new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
    };

    public Sprite setTexture(Texture texture){
        this.texture = texture;
        return this;
    }

    public Sprite setTexCoords(Vector2f[] texCoords){
        this.texCoords = texCoords;
        return this;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId(){
        return texture == null ? -1 : texture.getId();
    }
}

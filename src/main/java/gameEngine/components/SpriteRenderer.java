package gameEngine.components;

import gameEngine.ImGuiLayer;
import gameEngine.Transform;
import gameEngine.abstracts.Component;
import gameEngine.renderer.Texture;
import gameEngine.sprites.Sprite;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.sql.SQLOutput;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;
    private Transform lastT;
    private boolean isDirty = true;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Texture texture){
        this.sprite = new Sprite(texture);
        this.color = new Vector4f(1, 1, 1, 1);;
    }

    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {
        this.lastT = super.gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastT.equals(super.gameObject.transform)){
            this.gameObject.transform.copy(lastT);
            this.isDirty = true;
        }
    }

    @Override
    public void imgui(){

    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector4f getColor() {
        return color;
    }

    public Vector2f[] getTexCoords(){
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)){
            this.color.set(color);
            isDirty = true;
        }
    }

    public boolean isDirty(){
        return this.isDirty;
    }

    public void setClean(){
        this.isDirty = false;
    }
}

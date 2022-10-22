package gameEngine.components;

import gameEngine.ImGuiLayer;
import gameEngine.Transform;
import gameEngine.abstracts.Component;
import gameEngine.renderer.Texture;
import gameEngine.sprites.Sprite;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastT;
    private transient boolean isDirty = true;

    /*
    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Texture texture){
        this.sprite = new Sprite(texture);
        this.color = new Vector4f(1, 1, 1, 1);
    }


    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

     */

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
        float[] imColor = {color.x, color.y, color.z, color.w};

        ImGui.pushFont(ImGuiLayer.fonts.get(ImGuiLayer.fonts.size()-1));
        ImGui.text("Color Picker:");
        if (ImGui.colorPicker4("Color Picker", imColor)) {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
        ImGui.popFont();
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

    public SpriteRenderer setSprite(Sprite sprite){
        this.sprite = sprite;
        isDirty = true;
        return this;
    }

    public SpriteRenderer setColor(Vector4f color) {
        if (!this.color.equals(color)){
            this.color.set(color);
            isDirty = true;
        }
        return this;
    }


    public SpriteRenderer setTexture(Texture texture){
        this.sprite = new Sprite().setTexture(texture);
        isDirty = true;
        return this;
    }
    public boolean isDirty(){
        return this.isDirty;
    }

    public void setClean(){
        this.isDirty = false;
    }
}

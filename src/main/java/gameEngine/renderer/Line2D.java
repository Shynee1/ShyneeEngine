package gameEngine.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {

    private Vector2f start;
    private Vector2f end;
    private Vector3f color;
    private int lifetime;

    public Line2D(Vector2f start, Vector2f end, Vector3f color, int lifetime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }

    public int beginFrame(){
        lifetime--;
        return lifetime;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector2f getStart() {
        return start;
    }
}

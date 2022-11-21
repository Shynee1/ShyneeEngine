package gameEngine.util;

import org.joml.Vector2f;

public class JMath {

    public static Vector2f rotatePoint(Vector2f center, Vector2f point, double degrees){

        double degreesToRadians = Math.toRadians(degrees);

        float newX = (float) (center.x + (point.x-center.x)*Math.cos(degreesToRadians) - (point.y-center.y)*Math.sin(degreesToRadians));
        float newY = (float) (center.y + (point.x-center.x)*Math.sin(degreesToRadians) + (point.y-center.y)*Math.cos(degreesToRadians));

        return new Vector2f(newX, newY);
    }
}

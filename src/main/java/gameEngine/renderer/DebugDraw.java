package gameEngine.renderer;

import gameEngine.Window;
import gameEngine.util.AssetPool;
import gameEngine.util.JMath;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {

    private static final int MAX_LINES = 500;

    //6 floats per vertex, 2 vertices per line
    private static final float[] vertexArray = new float[MAX_LINES*6*2];
    private static final Shader shader = AssetPool.getShader("assets/shaders/debugLine.glsl");

    private static List<Line2D> lines = new ArrayList<>();

    private static int vaoId;
    private static int vboId;

    private static boolean started = false;

    public static void start(){
        //Generate vao
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Generate vbo and free memory
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Enable vertex array attributes
        //[x,y,z, r,g,b]
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame(){
        if (!started) {
            start();
            started = true;
        }

        //Remove "dead" lines
        for (int i = 0; i < lines.size(); i++){
            if (lines.get(i).beginFrame() < 0){
                lines.remove(i);
                i--;
            }
        }

    }

    public static void draw(){
        if (lines.size() == 0) return;

        int index = 0;
        for (Line2D line : lines){
            for (int i = 0; i < 2; i++){
                Vector2f position = i == 0 ? line.getStart() : line.getEnd();
                Vector3f color = line.getColor();

                //Add x, y, z to vertex array
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;

                index += 6;
            }

            //Bind vbo to local array (vertexArray)
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

            //Use shader to draw
            shader.use();
            shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
            shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

            //Bind vao
            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            //Draw the lines
            glDrawArrays(GL_LINES, 0, lines.size()*6*2);

            //Disable location
            glDisableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glBindVertexArray(0);

            shader.detach();
        }
    }

    public static void addLine2D(Vector2f start, Vector2f end){
        //TODO: ADD CONSTANTS FOR COMMON COLORS
        addLine2D(start, end, new Vector3f(1, 0, 0), 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color){
        addLine2D(start, end, color, 1);
    }

    public static void addLine2D(Vector2f start, Vector2f end, int lifetime){
        addLine2D(start, end, new Vector3f(1, 0, 0), lifetime);
    }

    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color, int lifetime){
        DebugDraw.lines.add(new Line2D(start, end, color, lifetime));
    }


    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color, int lifetime){
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y)
        };

        for (int i = 0; i < vertices.length; i++){
            Vector2f start = JMath.rotatePoint(center, vertices[i], rotation);

            if (i != vertices.length-1) {
                addLine2D(start, JMath.rotatePoint(center, vertices[i+1], rotation), color, lifetime);
            }
            else addLine2D(start, JMath.rotatePoint(center, vertices[0], rotation), color, lifetime);
        }
    }
}

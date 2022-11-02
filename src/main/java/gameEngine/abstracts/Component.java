package gameEngine.abstracts;

import gameEngine.objects.GameObject;
import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    //Global
    private static int ID_COUNTER = 0;
    //Unique to each object
    private int uid = -1;

    public transient GameObject gameObject = null;

    public void start(){

    }

    public void update(float dt){

    }

    public void imgui(){
        try{
            //Get fields of component inheriting this
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field f : fields){
                if(Modifier.isTransient(f.getModifiers())) continue;

                boolean isPrivate = Modifier.isPrivate(f.getModifiers());
                if (isPrivate){
                    f.setAccessible(true);
                }

                Class<?> type = f.getType();
                Object value = f.get(this);
                String name = f.getName();

                if (type == int.class){
                    int val = (int) value;
                    int[] imInt = {val};
                    if(ImGui.dragInt(name + ": ", imInt)){
                        f.set(this, imInt[0]);
                    }
                } else if (type == float.class){
                    float val = (float)value;
                    float[] imFloat = {val};
                    if(ImGui.dragFloat(name + ": ", imFloat)){
                        f.set(this, imFloat[0]);
                    }
                } else if (type == boolean.class){
                    boolean val = (boolean)value;
                    if (ImGui.checkbox(name+": ", val)){
                        f.set(this,!val);
                    }
                } else if (type == Vector3f.class){
                    Vector3f val = (Vector3f) value;
                    float[] imVec3 = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name+": ", imVec3)){
                        val.set(imVec3[0], imVec3[1], imVec3[2]);
                    }
                } else if (type == Vector4f.class){
                    Vector4f val = (Vector4f) value;
                    float[] imVec4 = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name+": ", imVec4)){
                        val.set(imVec4[0], imVec4[1], imVec4[2], imVec4[3]);
                    }
                }

                if (isPrivate)f.setAccessible(false);
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }

    }

    public void generateId(){
        if (uid == -1){
            this.uid = Component.ID_COUNTER++;
        }
    }

    public int getUid(){
        return this.uid;
    }

    public static void init(int maxId){
        Component.ID_COUNTER = maxId;
    }
}

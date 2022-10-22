package gameEngine.serialization;

import com.google.gson.*;
import gameEngine.Transform;
import gameEngine.abstracts.Component;
import gameEngine.objects.GameObject;

import java.lang.reflect.Type;

/**
 * Responsible for custom serialization/deserialization of GameObjects with Gson
 */
public class GameObjectSerialization implements JsonDeserializer<GameObject> {

    /**
     * Deserializes GameObject with custom Component deserializer
     *
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return       Deserialized GameObject
     * @throws JsonParseException
     */
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject go = new GameObject(name, transform, zIndex);
        for (JsonElement e : components){
            //Deserialize component using custom deserializer
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }

        return go;
    }
}

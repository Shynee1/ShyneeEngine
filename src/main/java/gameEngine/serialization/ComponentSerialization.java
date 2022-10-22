package gameEngine.serialization;

import com.google.gson.*;
import gameEngine.abstracts.Component;

import java.lang.reflect.Type;

/**
 * Responsible for custom serialization/deserialization of Components with Gson
 */
public class ComponentSerialization implements JsonSerializer<Component>, JsonDeserializer<Component> {

    /**
     * Adds "type" property to specify the Component type (ex: "type": SpriteRenderer)
     *
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context
     * @return Serialized JsonElement
     */
    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject res = new JsonObject();
        res.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        res.add("properties", context.serialize(src, src.getClass()));
        return res;
    }

    /**
     * Correctly deserializes Component based on Component type
     *
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return Deserialized Component
     * @throws JsonParseException
     */
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject res = json.getAsJsonObject();
        String type = res.get("type").getAsString();

        JsonElement element = res.get("properties");
        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }
}

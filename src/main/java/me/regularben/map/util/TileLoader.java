package me.regularben.map.util;

import com.google.gson.*;
import java.util.Map;

public class TileLoader {

    public static TileType load(JsonObject json) {

        String id = json.get("id").getAsString();
        boolean walkable = json.get("walkable").getAsBoolean();

        TileType tile = new TileType(id, walkable);

        if (json.has("texture")) {
            String texturePath = json.get("texture").getAsString();
            tile.setTexture(TextureLoader.load(texturePath));
        }

        JsonArray behaviors = json.getAsJsonArray("behaviors");
        if (behaviors != null) {
            for (JsonElement element : behaviors) {
                JsonObject obj = element.getAsJsonObject();
                String type = obj.get("type").getAsString();

                Map<String, Object> data = new Gson().fromJson(obj, Map.class);
                TileBehavior behavior = BehaviorRegistry.create(type, data);

                tile.addBehavior(behavior);
            }
        }

        return tile;
    }
}

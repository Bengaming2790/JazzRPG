package me.regularben.map.util;

import java.util.HashMap;
import java.util.Map;

public class TileRegistry {

    private static final Map<String, TileType> tiles = new HashMap<>();
    private static TileType fallback = null;

    public static TileType register(TileType tile) {
        tiles.put(tile.getId(), tile);
        tile.lock();
        return tile;
    }


    public static void setFallback(TileType tile) {
        fallback = tile;
    }

    public static TileType get(String id) {
        TileType tile = tiles.get(id);
        if (tile == null) {
            if (fallback != null) return fallback;
            throw new RuntimeException("Unknown tile ID: " + id);
        }
        return tile;
    }
    public static java.util.Collection<TileType> getAll() {
        return java.util.Collections.unmodifiableCollection(tiles.values());
    }
    public static boolean contains(String id) {
        return tiles.containsKey(id);
    }
}
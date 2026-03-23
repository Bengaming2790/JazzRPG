package me.regularben.map;

import me.regularben.map.util.Tile;
import me.regularben.map.util.TileRegistry;
import me.regularben.map.util.TileType;

import java.nio.file.*;
import java.util.List;

public class TileMapLoader {

    public static final int COLS = 80;
    public static final int ROWS = 45;


    private static final java.util.Map<Integer, String> INT_ID_MAP = new java.util.HashMap<>();

    public static void mapIntId(int intId, String stringId) {
        INT_ID_MAP.put(intId, stringId);
    }

    public static Tile[][] load(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            Tile[][] map = new Tile[ROWS][COLS];

            // fallback tile for gaps
            TileType fallback = TileRegistry.get("air");

            for (int row = 0; row < Math.min(lines.size(), ROWS); row++) {
                String line = lines.get(row).trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("[,\\s]+");

                for (int col = 0; col < COLS; col++) {
                    TileType type;

                    if (col < parts.length) {
                        String token = parts[col].trim();
                        type = resolveToken(token);
                    } else {
                        type = fallback;
                    }

                    map[row][col] = new Tile(type, col, row);
                }
            }

            // Fill any completely missing rows
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (map[row][col] == null) {
                        map[row][col] = new Tile(fallback, col, row);
                    }
                }
            }

            return map;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load tilemap: " + filePath, e);
        }
    }


    private static TileType resolveToken(String token) {
        // Try integer lookup first
        try {
            int intId = Integer.parseInt(token);
            String stringId = INT_ID_MAP.get(intId);
            if (stringId == null) {
                throw new RuntimeException(
                        "No string ID mapped for integer tile ID: " + intId +
                                ". Call TileMapLoader.mapIntId() before loading."
                );
            }
            return TileRegistry.get(stringId);
        } catch (NumberFormatException e) {
            return TileRegistry.get(token);
        }
    }
}
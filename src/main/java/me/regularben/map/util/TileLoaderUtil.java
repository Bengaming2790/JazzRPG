package me.regularben.map.util;

import java.io.FileReader;
import java.nio.file.*;
import com.google.gson.*;

public class TileLoaderUtil {

    public static void loadAll(String folderPath) {
        try {
            Path folder = Paths.get(folderPath);

            if (!Files.exists(folder)) {
                System.out.println("Creating missing folder: " + folderPath);
                Files.createDirectories(folder);
                return;
            }

            Files.list(folder)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        try (FileReader reader = new FileReader(path.toFile())) {

                            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                            TileType tile = TileLoader.load(json);

                            TileRegistry.register(tile);

                            System.out.println("Loaded tile: " + tile.getId());

                        } catch (Exception e) {
                            System.err.println("Failed to load tile: " + path);
                            e.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
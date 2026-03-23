package me.regularben.map.util;

import com.google.gson.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;

public class InternalTileLoader {

    public static void loadAll(String folder) {
        try {
            URL folderUrl = InternalTileLoader.class.getClassLoader().getResource(folder);

            if (folderUrl == null) {
                System.out.println("[InternalTileLoader] No builtin tile folder found at: " + folder + ", skipping.");
                return;
            }

            URI uri = folderUrl.toURI();

            if (uri.getScheme().equals("jar")) {
                try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    Path folderPath = fs.getPath(folder);
                    scanAndLoad(folderPath);
                }
            } else {
                Path folderPath = Paths.get(uri);
                scanAndLoad(folderPath);
            }

        } catch (Exception e) {
            System.err.println("[InternalTileLoader] Failed to load builtin tiles from: " + folder);
            e.printStackTrace();
        }
    }

    private static void scanAndLoad(Path folderPath) throws IOException {
        if (!Files.exists(folderPath)) {
            System.out.println("[InternalTileLoader] Folder does not exist: " + folderPath + ", skipping.");
            return;
        }

        Files.walk(folderPath)
                .filter(p -> p.toString().endsWith(".json"))
                .forEach(p -> {
                    try (InputStream is = Files.newInputStream(p)) {
                        JsonObject json = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
                        TileType tile = TileLoader.load(json);
                        TileRegistry.register(tile);
                        System.out.println("[InternalTileLoader] Loaded builtin tile: " + tile.getId());
                    } catch (Exception e) {
                        System.err.println("[InternalTileLoader] Failed to load: " + p);
                        e.printStackTrace();
                    }
                });
    }
}
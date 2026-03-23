package me.regularben.map.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {

    private static final Map<String, BufferedImage> cache = new HashMap<>();
    private static final String TEXTURE_ROOT = "assets/textures/";

    public static BufferedImage load(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        String resourcePath = TEXTURE_ROOT + path;
        try (InputStream stream = TextureLoader.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (stream != null) {
                BufferedImage img = ImageIO.read(stream);
                cache.put(path, img);
                return img;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load texture from classpath: " + resourcePath, e);
        }

        // Fall back to filesystem
        try {
            java.io.File file = new java.io.File(resourcePath);
            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                cache.put(path, img);
                return img;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load texture from filesystem: " + resourcePath, e);
        }

        throw new RuntimeException("Texture not found in classpath or filesystem: " + resourcePath);
    }

    public static void clearCache() {
        cache.clear();
    }
}
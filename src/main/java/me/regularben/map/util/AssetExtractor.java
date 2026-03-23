package me.regularben.map.util;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;

public class AssetExtractor {

    public static void extractFolder(String resourceFolder, String outputFolder) {
        try {
            URL folderUrl = AssetExtractor.class.getClassLoader().getResource(resourceFolder);

            if (folderUrl == null) {
                System.out.println("[AssetExtractor] No resource folder found at: " + resourceFolder + ", skipping.");
                return;
            }

            Files.createDirectories(Paths.get(outputFolder));

            URI uri = folderUrl.toURI();

            if (uri.getScheme().equals("jar")) {
                try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    Path folderPath = fs.getPath(resourceFolder);
                    extractAll(folderPath, resourceFolder, outputFolder);
                }
            } else {
                Path folderPath = Paths.get(uri);
                extractAll(folderPath, resourceFolder, outputFolder);
            }

        } catch (Exception e) {
            System.err.println("[AssetExtractor] Extraction failed for: " + resourceFolder);
            e.printStackTrace();
        }
    }

    private static void extractAll(Path folderPath, String resourceFolder, String outputFolder) throws Exception {
        if (!Files.exists(folderPath)) {
            System.out.println("[AssetExtractor] Resource folder does not exist: " + folderPath + ", skipping.");
            return;
        }

        Files.walk(folderPath)
                .filter(Files::isRegularFile)
                .forEach(sourcePath -> {
                    try {
                        String relativePath = folderPath.relativize(sourcePath).toString();
                        Path outputPath = Paths.get(outputFolder, relativePath);

                        if (Files.exists(outputPath)) return;

                        Files.createDirectories(outputPath.getParent());

                        String resourcePath = resourceFolder + "/" + relativePath;
                        try (InputStream stream = AssetExtractor.class.getClassLoader()
                                .getResourceAsStream(resourcePath)) {

                            if (stream == null) {
                                System.err.println("[AssetExtractor] Could not open stream for: " + resourcePath);
                                return;
                            }

                            Files.copy(stream, outputPath);
                            System.out.println("[AssetExtractor] Extracted: " + outputPath);
                        }

                    } catch (Exception e) {
                        System.err.println("[AssetExtractor] Failed to extract: " + sourcePath);
                        e.printStackTrace();
                    }
                });
    }
}
package me.regularben;

import me.regularben.editor.MapEditor;
import me.regularben.entity.Player;
import me.regularben.map.MapCoord;
import me.regularben.map.MapPanel;
import me.regularben.map.TileMapLoader;
import me.regularben.map.WorldMap;
import me.regularben.map.util.*;
import me.regularben.util.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean debug = true;
        if (debug) {
            deleteDirectory(Paths.get("assets"));
            System.out.println("[Debug] Cleared assets for re-extraction.");
        }

        Files.createDirectories(Paths.get("assets/tiles"));
        Files.createDirectories(Paths.get("assets/textures/tiles"));
        Files.createDirectories(Paths.get("assets/maps"));

        AssetExtractor.extractFolder("assets/textures/tiles", "assets/textures/tiles");
        AssetExtractor.extractFolder("assets/maps",           "assets/maps");
        AssetExtractor.extractFolder("assets/tiles",          "assets/tiles");

        DefaultBehaviors.init();
        InternalTileLoader.loadAll("tiles");
        TileLoaderUtil.loadAll("assets/tiles");

        TileMapLoader.mapIntId(0, "grass");
        TileMapLoader.mapIntId(1, "water");
        TileMapLoader.mapIntId(2, "dirt");
        TileMapLoader.mapIntId(3, "stone");

        WorldMap worldMap = new WorldMap("assets/maps");
        worldMap.init();


        Player player = new Player();
        player.debug = debug;
        player.setWorldMap(worldMap);

        Input input = new Input();
        MapPanel panel = new MapPanel(worldMap, 16);
        panel.setPlayer(player);
        panel.addKeyListener(input);
        panel.setFocusable(true);

        JFrame game = new JFrame("Jazz RPG");
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setLayout(new BorderLayout());
        game.add(panel, BorderLayout.CENTER);
        game.pack();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        panel.requestFocusInWindow();

        Timer gameLoop = getTimer(player, game, panel);
        gameLoop.start();
    }

    private static Timer getTimer(Player player, JFrame game, MapPanel panel) {
        Timer gameLoop = new Timer(16, e -> {
            player.update();
            panel.updateCamera();

            if (Input.isKeyDown(KeyEvent.VK_ESCAPE)) System.exit(0);

            if (player.debug && Input.isKeyReleased(KeyEvent.VK_SLASH))
                System.out.println(game.getSize());

            if (player.debug && Input.isKeyReleased(KeyEvent.VK_F1))
                MapEditor.launch();

            panel.repaint();
            Input.endFrame();
        });

        gameLoop.setCoalesce(true);
        return gameLoop;
    }

    private static void deleteDirectory(Path path) {
        if (!Files.exists(path)) return;
        try {
            Files.walk(path)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (Exception e) {
                            System.err.println("[Debug] Failed to delete: " + p);
                        }
                    });
        } catch (Exception e) {
            System.err.println("[Debug] Failed to walk assets directory: " + path);
            e.printStackTrace();
        }
    }
}
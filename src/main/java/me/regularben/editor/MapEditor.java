package me.regularben.editor;

import me.regularben.map.util.*;
import me.regularben.map.TileMapLoader;

import javax.swing.*;
import java.awt.*;

public class MapEditor {

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            DefaultBehaviors.init();
            InternalTileLoader.loadAll("tiles");
            TileLoaderUtil.loadAll("assets/tiles");

            TileMapLoader.mapIntId(0, "grass");
            TileMapLoader.mapIntId(1, "water");
            TileMapLoader.mapIntId(2, "dirt");
            TileMapLoader.mapIntId(3, "stone");

            EditorState state = new EditorState(
                    TileMapLoader.COLS,
                    TileMapLoader.ROWS
            );

            EditorCanvas  canvas  = new EditorCanvas(state, 16);
            TilePalette   palette = new TilePalette(state);
            EditorToolbar toolbar = new EditorToolbar(state, canvas);

            JFrame frame = new JFrame("Map Editor");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(toolbar,                    BorderLayout.NORTH);
            frame.add(new JScrollPane(canvas),    BorderLayout.CENTER);
            frame.add(new JScrollPane(palette),   BorderLayout.EAST);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
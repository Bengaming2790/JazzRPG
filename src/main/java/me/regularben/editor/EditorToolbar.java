package me.regularben.editor;

import me.regularben.map.TileMapLoader;
import me.regularben.map.util.Tile;
import me.regularben.map.util.TileRegistry;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class EditorToolbar extends JPanel {

    public EditorToolbar(EditorState state, EditorCanvas canvas) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 6, 4));
        setBackground(new Color(50, 50, 50));

        JButton newBtn   = makeButton("New");
        JButton loadBtn  = makeButton("Load");
        JButton saveBtn  = makeButton("Save");
        JButton saveAsBtn = makeButton("Save As");
        JButton clearBtn = makeButton("Clear");

        JLabel fileLabel = new JLabel("No file loaded");
        fileLabel.setForeground(new Color(180, 180, 180));
        fileLabel.setFont(fileLabel.getFont().deriveFont(11f));

        add(newBtn);
        add(loadBtn);
        add(saveBtn);
        add(saveAsBtn);
        add(clearBtn);
        add(Box.createHorizontalStrut(12));
        add(fileLabel);

        newBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Clear the current map and start fresh?", "New Map",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clearMap(state);
                state.setCurrentFilePath(null);
                fileLabel.setText("New map");
                canvas.repaint();
            }
        });

        loadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("assets/maps");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                try {
                    Tile[][] loaded = TileMapLoader.load(path);
                    for (int row = 0; row < state.rows; row++)
                        for (int col = 0; col < state.cols; col++)
                            state.setTile(col, row, loaded[row][col] != null
                                    ? loaded[row][col].getType() : null);
                    state.setCurrentFilePath(path);
                    fileLabel.setText(chooser.getSelectedFile().getName());
                    canvas.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to load map:\n" + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveBtn.addActionListener(e -> {
            if (state.getCurrentFilePath() == null) {
                saveAsBtn.doClick();
            } else {
                saveMap(state, state.getCurrentFilePath(), fileLabel);
            }
        });

        saveAsBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("assets/maps");
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".txt")) path += ".txt";
                saveMap(state, path, fileLabel);
                state.setCurrentFilePath(path);
            }
        });

        clearBtn.addActionListener(e -> {
            clearMap(state);
            canvas.repaint();
        });
    }

    private void saveMap(EditorState state, String path, JLabel fileLabel) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < state.rows; row++) {
                for (int col = 0; col < state.cols; col++) {
                    var tile = state.getTile(col, row);
                    sb.append(tile != null ? tile.getType().getId() : "air");
                    if (col < state.cols - 1) sb.append(",");
                }
                sb.append("\n");
            }
            Files.writeString(Paths.get(path), sb.toString());
            fileLabel.setText(Paths.get(path).getFileName().toString());
            JOptionPane.showMessageDialog(this, "Saved to:\n" + path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save:\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearMap(EditorState state) {
        for (int row = 0; row < state.rows; row++)
            for (int col = 0; col < state.cols; col++)
                state.setTile(col, row, null);
    }

    private JButton makeButton(String label) {
        JButton btn = new JButton(label);
        btn.setBackground(new Color(70, 70, 70));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }
}
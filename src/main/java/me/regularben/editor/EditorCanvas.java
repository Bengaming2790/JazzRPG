package me.regularben.editor;

import me.regularben.map.util.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class EditorCanvas extends JPanel {

    private final EditorState state;
    private final int tileSize;

    private static final Color GRID_COLOR  = new Color(60, 60, 60);
    private static final Color HOVER_COLOR = new Color(255, 255, 255, 60);
    private static final Color ERASE_COLOR = new Color(255, 60, 60, 80);

    private int hoverCol = -1, hoverRow = -1;
    private boolean erasing = false;

    public EditorCanvas(EditorState state, int tileSize) {
        this.state    = state;
        this.tileSize = tileSize;

        setPreferredSize(new Dimension(state.cols * tileSize, state.rows * tileSize));
        setBackground(new Color(30, 30, 30));
        setToolTipText("");
        setFocusable(true);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                state.saveSnapshot();
                handleMouse(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!SwingUtilities.isMiddleMouseButton(e)) handleMouse(e);
            }

            @Override public void mouseMoved(MouseEvent e)  { updateHover(e); }
            @Override public void mouseExited(MouseEvent e) { hoverCol = hoverRow = -1; repaint(); }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        KeyStroke undoKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        getInputMap(WHEN_FOCUSED).put(undoKey, "undo");
        getActionMap().put("undo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                state.undo();
                repaint();
            }
        });
    }

    private void handleMouse(MouseEvent e) {
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;

        if (SwingUtilities.isMiddleMouseButton(e)) {
            if (state.getSelectedTile() != null)
                floodFill(col, row, state.getSelectedTile());
        } else if (SwingUtilities.isRightMouseButton(e)) {
            erasing = true;
            state.setTile(col, row, null);
        } else {
            erasing = false;
            if (state.getSelectedTile() != null)
                state.setTile(col, row, state.getSelectedTile());
        }

        updateHover(e);
        repaint();
    }

    private void updateHover(MouseEvent e) {
        hoverCol = e.getX() / tileSize;
        hoverRow = e.getY() / tileSize;
        erasing  = SwingUtilities.isRightMouseButton(e);
        repaint();
    }

    private void floodFill(int startCol, int startRow, TileType fillType) {
        if (state.outOfBounds(startCol, startRow)) return;

        var startTile = state.getTile(startCol, startRow);
        String targetId = startTile != null ? startTile.getType().getId() : null;
        String fillId   = fillType.getId();

        if (fillId.equals(targetId)) return;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startCol, startRow});

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int c = pos[0], r = pos[1];

            if (state.outOfBounds(c, r)) continue;

            var tile = state.getTile(c, r);
            String currentId = tile != null ? tile.getType().getId() : null;

            if (!Objects.equals(currentId, targetId)) continue;

            state.setTile(c, r, fillType);

            queue.add(new int[]{c + 1, r});
            queue.add(new int[]{c - 1, r});
            queue.add(new int[]{c, r + 1});
            queue.add(new int[]{c, r - 1});
        }
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;
        if (state.outOfBounds(col, row)) return null;
        var tile = state.getTile(col, row);
        return "[" + col + ", " + row + "] " + (tile != null ? tile.getType().getId() : "empty");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int row = 0; row < state.rows; row++) {
            for (int col = 0; col < state.cols; col++) {
                var tile = state.getTile(col, row);
                int x = col * tileSize;
                int y = row * tileSize;

                if (tile != null) {
                    BufferedImage img = tile.getType().getTexture();
                    if (img != null) {
                        g2.drawImage(img, x, y, tileSize, tileSize, null);
                    } else {
                        g2.setColor(Color.MAGENTA);
                        g2.fillRect(x, y, tileSize, tileSize);
                        g2.setColor(Color.BLACK);
                        g2.drawString(tile.getType().getId().substring(0, 1).toUpperCase(), x + 4, y + 12);
                    }
                }

                g2.setColor(GRID_COLOR);
                g2.drawRect(x, y, tileSize, tileSize);
            }
        }

        if (hoverCol >= 0 && hoverRow >= 0
                && hoverCol < state.cols && hoverRow < state.rows) {
            g2.setColor(erasing ? ERASE_COLOR : HOVER_COLOR);
            g2.fillRect(hoverCol * tileSize, hoverRow * tileSize, tileSize, tileSize);
        }
    }
}

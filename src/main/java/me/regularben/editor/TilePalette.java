package me.regularben.editor;

import me.regularben.map.util.TileRegistry;
import me.regularben.map.util.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TilePalette extends JPanel {

    private static final int CELL_SIZE = 48;
    private static final int PADDING   = 6;

    public TilePalette(EditorState state) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 40));
        setPreferredSize(new Dimension(120, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Tiles");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(8));

        for (TileType type : TileRegistry.getAll()) {
            add(makeTileButton(type, state));
            add(Box.createVerticalStrut(PADDING));
        }
    }

    private JButton makeTileButton(TileType type, EditorState state) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage img = type.getTexture();
                if (img != null) {
                    g.drawImage(img, 4, 4, CELL_SIZE - 8, CELL_SIZE - 8, null);
                } else {
                    g.setColor(Color.MAGENTA);
                    g.fillRect(4, 4, CELL_SIZE - 8, CELL_SIZE - 8);
                }
                // Highlight selected
                if (state.getSelectedTile() == type) {
                    g.setColor(new Color(255, 200, 0));
                    ((Graphics2D) g).setStroke(new BasicStroke(2));
                    g.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
                }
            }
        };

        btn.setToolTipText(type.getId());
        btn.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        btn.setMaximumSize(new Dimension(CELL_SIZE, CELL_SIZE));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(55, 55, 55));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            state.setSelectedTile(type);
            // Repaint all palette buttons to update highlight
            for (Component c : getComponents()) c.repaint();
        });

        return btn;
    }
}
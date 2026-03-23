package me.regularben.map.render;

import me.regularben.map.util.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileRenderer {

    private final int tileSize;

    public TileRenderer(int tileSize) {
        this.tileSize = tileSize;
    }

    public void render(Graphics2D g, Tile[][] map, int offsetX, int offsetY) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                Tile tile = map[row][col];
                if (tile == null) continue;

                BufferedImage img = tile.getType().getTexture();
                if (img == null) continue;

                int x = col * tileSize - offsetX;
                int y = row * tileSize - offsetY;

                g.drawImage(img, x, y, tileSize, tileSize, null);
            }
        }
    }
}

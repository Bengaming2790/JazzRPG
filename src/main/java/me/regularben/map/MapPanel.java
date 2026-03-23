package me.regularben.map;

import me.regularben.entity.Player;
import me.regularben.map.render.TileRenderer;
import me.regularben.map.util.Tile;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {

    private final WorldMap     worldMap;
    private final TileRenderer renderer;
    private final int          tileSize;

    private Player player;
    private double cameraX = 0, cameraY = 0;

    public static final int BASE_WIDTH  = 1280;
    public static final int BASE_HEIGHT = 720;

    public MapPanel(WorldMap worldMap, int tileSize) {
        this.worldMap = worldMap;
        this.tileSize = tileSize;
        this.renderer = new TileRenderer(tileSize);
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setBackground(Color.BLACK);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void updateCamera() {
        if (player == null) return;
        // Camera tracks player in world-pixel space
        cameraX = player.getX() * tileSize - BASE_WIDTH  / 2.0 + tileSize / 2.0;
        cameraY = player.getY() * tileSize - BASE_HEIGHT / 2.0 + tileSize / 2.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        double scaleX = (double) getWidth()  / BASE_WIDTH;
        double scaleY = (double) getHeight() / BASE_HEIGHT;
        double scale  = Math.min(scaleX, scaleY);

        g2.translate((getWidth()  - BASE_WIDTH  * scale) / 2.0,
                (getHeight() - BASE_HEIGHT * scale) / 2.0);
        g2.scale(scale, scale);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        for (var entry : worldMap.getLoadedMaps().entrySet()) {
            MapCoord coord  = entry.getKey();
            Tile[][] tiles  = entry.getValue();
            if (tiles == null) continue;

            int mapOffsetX = coord.x * TileMapLoader.COLS * tileSize;
            int mapOffsetY = coord.y * TileMapLoader.ROWS * tileSize;

            // Subtract camera to get screen position
            int screenOffsetX = (int)(mapOffsetX - cameraX);
            int screenOffsetY = (int)(mapOffsetY - cameraY);

            renderer.render(g2, tiles, -screenOffsetX, -screenOffsetY);
        }

        if (player != null && player.getSprite() != null) {
            int drawX = BASE_WIDTH  / 2 - player.getSpriteWidth()  / 2;
            int drawY = BASE_HEIGHT / 2 - player.getSpriteHeight() / 2;
            g2.drawImage(player.getSprite(), drawX, drawY,
                    player.getSpriteWidth(), player.getSpriteHeight(), null);
        }

        g2.dispose();
    }

    public Point screenToWorld(int screenX, int screenY) {
        double scaleX = (double) getWidth()  / BASE_WIDTH;
        double scaleY = (double) getHeight() / BASE_HEIGHT;
        double scale  = Math.min(scaleX, scaleY);

        double translateX = (getWidth()  - BASE_WIDTH  * scale) / 2.0;
        double translateY = (getHeight() - BASE_HEIGHT * scale) / 2.0;

        int worldX = (int)((screenX - translateX) / scale + cameraX);
        int worldY = (int)((screenY - translateY) / scale + cameraY);

        return new Point(worldX, worldY);
    }
}
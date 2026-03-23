package me.regularben.map;

import me.regularben.entity.Entity;

public class TileManager {

    private final Tile[][] tiles;
    private final int width, height;

    public TileManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public void setTile(int x, int y, TileType type) {
        tiles[x][y] = new Tile(type, x, y);
    }

    public Tile getTile(int x, int y) {
        if (outOfBounds(x, y)) return null;
        return tiles[x][y];
    }

    public boolean canMoveTo(int x, int y) {
        Tile tile = getTile(x, y);
        return tile != null && tile.isWalkable();
    }

    public void step(Entity entity, int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null) {
            tile.onStep(entity);
        }
    }

    private boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= width || y >= height;
    }
}
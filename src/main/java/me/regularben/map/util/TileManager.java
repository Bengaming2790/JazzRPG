package me.regularben.map.util;

import me.regularben.entity.Entity;

public class TileManager {

    private final Tile[][] tiles;
    private final int cols, rows;

    public TileManager(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.tiles = new Tile[rows][cols]; // rows first
    }

    public void setTile(int col, int row, TileType type) {
        if (outOfBounds(col, row)) return;
        tiles[row][col] = new Tile(type, col, row);
    }

    public Tile getTile(int col, int row) {
        if (outOfBounds(col, row)) return null;
        return tiles[row][col];
    }

    public boolean canMoveTo(int col, int row) {
        Tile tile = getTile(col, row);
        return tile != null && tile.isWalkable();
    }

    public void step(Entity entity, int col, int row) {
        Tile tile = getTile(col, row);
        if (tile != null) tile.onStep(entity);
    }

    public void enter(Entity entity, int col, int row) {
        Tile tile = getTile(col, row);
        if (tile != null) tile.onEnter(entity);
    }

    public void exit(Entity entity, int col, int row) {
        Tile tile = getTile(col, row);
        if (tile != null) tile.onExit(entity);
    }

    private boolean outOfBounds(int col, int row) {
        return col < 0 || row < 0 || col >= cols || row >= rows;
    }
}
package me.regularben.map.util;

import me.regularben.entity.Entity;

public class Tile {

    private final TileType type;
    private final int x, y;

    public Tile(TileType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }
    public void onEnter(Entity entity) {
        type.onEnter(entity, this);
    }

    public void onExit(Entity entity) {
        type.onExit(entity, this);
    }
    public TileType getType() {
        return type;
    }

    public boolean isWalkable() {
        return type.isWalkable();
    }

    public void onStep(Entity entity) {
        type.onStep(entity, this);
    }
}
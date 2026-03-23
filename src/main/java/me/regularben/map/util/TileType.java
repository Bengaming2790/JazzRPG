package me.regularben.map;

import me.regularben.entity.Entity;
import me.regularben.map.TileBehavior;

import java.util.ArrayList;
import java.util.List;

public class TileType {

    private final String id;
    private final boolean walkable;
    private final List<TileBehavior> behaviors = new ArrayList<>();

    public TileType(String id, boolean walkable) {
        this.id = id;
        this.walkable = walkable;
    }

    public String getId() {
        return id;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void addBehavior(TileBehavior behavior) {
        behaviors.add(behavior);
    }

    public void onStep(Entity entity, Tile tile) {
        for (TileBehavior behavior : behaviors) {
            behavior.onStep(entity, tile);
        }
    }
}
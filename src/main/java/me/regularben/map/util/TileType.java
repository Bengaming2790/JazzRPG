package me.regularben.map.util;

import me.regularben.entity.Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileType {

    private final String id;
    private final boolean walkable;
    private final List<TileBehavior> behaviors = new ArrayList<>();
    private BufferedImage texture;
    private boolean locked = false;

    public TileType(String id, boolean walkable) {
        this.id = id;
        this.walkable = walkable;
    }


    void lock() {
        this.locked = true;
    }

    private void checkLocked() {
        if (locked) throw new IllegalStateException(
                "TileType '" + id + "' is already registered and cannot be modified."
        );
    }

    public String getId() {
        return id;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void addBehavior(TileBehavior behavior) {
        checkLocked();
        behaviors.add(behavior);
    }

    public void setTexture(BufferedImage texture) {
        checkLocked();
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public List<TileBehavior> getBehaviors() {
        return Collections.unmodifiableList(behaviors);
    }

    public void onStep(Entity entity, Tile tile) {
        for (TileBehavior behavior : behaviors) {
            behavior.onStep(entity, tile);
        }
    }

    public void onEnter(Entity entity, Tile tile) {
        for (TileBehavior behavior : behaviors) {
            behavior.onEnter(entity, tile);
        }
    }

    public void onExit(Entity entity, Tile tile) {
        for (TileBehavior behavior : behaviors) {
            behavior.onExit(entity, tile);
        }
    }
}
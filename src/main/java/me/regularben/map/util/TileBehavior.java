package me.regularben.map.util;

import me.regularben.entity.Entity;
public interface TileBehavior {

    default void onStep(Entity entity, Tile tile) {}

    default void onEnter(Entity entity, Tile tile) {}

    default void onExit(Entity entity, Tile tile) {}
}

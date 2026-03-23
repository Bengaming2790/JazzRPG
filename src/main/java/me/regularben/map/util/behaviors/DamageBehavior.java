package me.regularben.map.util.behaviors;

import me.regularben.entity.Entity;
import me.regularben.map.util.Tile;
import me.regularben.map.util.TileBehavior;

public class DamageBehavior implements TileBehavior {

    private final int damage;

    public DamageBehavior(int damage) {
        this.damage = damage;
    }

    @Override
    public void onStep(Entity entity, Tile tile) {
        entity.damage(damage);
    }
}

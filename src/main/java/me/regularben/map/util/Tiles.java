package me.regularben.map.util;

import me.regularben.map.util.behaviors.DamageBehavior;

public class Tiles {

    public static final TileType GRASS;
    public static final TileType LAVA;
    public static final TileType WATER;
    static {
        GRASS = TileRegistry.register(new TileType("grass", true));
        WATER = TileRegistry.register(new TileType("water", true));
        LAVA = TileRegistry.register(new TileType("lava", true));
        LAVA.addBehavior(new DamageBehavior(5));

    }
}

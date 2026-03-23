package me.regularben.map.util;

import me.regularben.map.util.behaviors.DamageBehavior;

public class DefaultBehaviors {

    public static void init() {

        BehaviorRegistry.register("damage", data -> {
            int amount = ((Number) data.getOrDefault("amount", 1)).intValue();
            return new DamageBehavior(amount);
        });


    }
}

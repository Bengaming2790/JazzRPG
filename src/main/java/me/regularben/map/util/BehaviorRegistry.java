package me.regularben.map.util;

import java.util.HashMap;
import java.util.Map;

public class BehaviorRegistry {

    private static final Map<String, BehaviorFactory> factories = new HashMap<>();

    public static void register(String id, BehaviorFactory factory) {
        factories.put(id, factory);
    }

    public static TileBehavior create(String id, Map<String, Object> data) {
        BehaviorFactory factory = factories.get(id);
        if (factory == null) {
            throw new RuntimeException("Unknown behavior: " + id);
        }
        return factory.create(data);
    }
}
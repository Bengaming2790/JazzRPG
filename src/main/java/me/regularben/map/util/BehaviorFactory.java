package me.regularben.map.util;

import java.util.Map;

public interface BehaviorFactory {
    TileBehavior create(Map<String, Object> data);
}

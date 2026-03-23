package me.regularben.map;

import java.util.Objects;

public class MapCoord {

    public final int x, y;

    public MapCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MapCoord offset(int dx, int dy) {
        return new MapCoord(x + dx, y + dy);
    }

    public String toFileName() {
        if (x == 0 && y == 0) return "map.txt";
        return "map," + x + "," + y + ".txt";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapCoord c)) return false;
        return x == c.x && y == c.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "MapCoord(" + x + ", " + y + ")";
    }
}
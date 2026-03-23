package me.regularben.map;

import me.regularben.map.util.Tile;
import me.regularben.map.util.TileManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldMap {

    private final String mapFolder;
    private final Map<MapCoord, Tile[][]>    loadedMaps = new HashMap<>();
    private final Map<MapCoord, TileManager> managers   = new HashMap<>();

    private MapCoord currentCoord;

    public WorldMap(String mapFolder) {
        this.mapFolder    = mapFolder;
        this.currentCoord = new MapCoord(0, 0);
    }


    public Tile[][] getMap(MapCoord coord) {
        if (loadedMaps.containsKey(coord)) return loadedMaps.get(coord);

        String path = mapFolder + "/" + coord.toFileName();
        if (!Files.exists(Paths.get(path))) return null;

        try {
            Tile[][] tiles = TileMapLoader.load(path);
            loadedMaps.put(coord, tiles);

            TileManager manager = new TileManager(TileMapLoader.COLS, TileMapLoader.ROWS);
            for (int row = 0; row < TileMapLoader.ROWS; row++)
                for (int col = 0; col < TileMapLoader.COLS; col++)
                    if (tiles[row][col] != null)
                        manager.setTile(col, row, tiles[row][col].getType());
            managers.put(coord, manager);

            System.out.println("[WorldMap] Loaded: " + coord.toFileName());
            return tiles;

        } catch (Exception e) {
            System.err.println("[WorldMap] Failed to load: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public TileManager getManager(MapCoord coord) {
        getMap(coord);
        return managers.get(coord);
    }

    private void unload(MapCoord coord) {
        if (loadedMaps.remove(coord) != null) {
            managers.remove(coord);
            System.out.println("[WorldMap] Unloaded: " + coord.toFileName());
        }
    }


    private Set<MapCoord> getDesiredCoords(MapCoord center) {
        Set<MapCoord> desired = new HashSet<>();
        desired.add(center);
        desired.add(center.offset( 1,  0));
        desired.add(center.offset(-1,  0));
        desired.add(center.offset( 0,  1));
        desired.add(center.offset( 0, -1));
        return desired;
    }

    public void updateLoadedMaps(MapCoord center) {
        this.currentCoord = center;
        Set<MapCoord> desired = getDesiredCoords(center);
        for (MapCoord coord : desired) getMap(coord);

        Set<MapCoord> toUnload = new HashSet<>(loadedMaps.keySet());
        toUnload.removeAll(desired);
        for (MapCoord coord : toUnload) unload(coord);
    }

    public void init() {
        updateLoadedMaps(currentCoord);
    }


    public MapCoord coordAt(double worldX, double worldY) {
        int mx = (int) Math.floor(worldX / TileMapLoader.COLS);
        int my = (int) Math.floor(worldY / TileMapLoader.ROWS);
        return new MapCoord(mx, my);
    }


    public Tile getTileAt(double worldX, double worldY) {
        MapCoord coord = coordAt(worldX, worldY);
        Tile[][] tiles = getMap(coord);
        if (tiles == null) return null;

        int localCol = (int)(worldX - coord.x * TileMapLoader.COLS);
        int localRow = (int)(worldY - coord.y * TileMapLoader.ROWS);

        if (localCol < 0 || localRow < 0
                || localCol >= TileMapLoader.COLS
                || localRow >= TileMapLoader.ROWS) return null;

        return tiles[localRow][localCol];
    }


    public boolean canMoveTo(double worldX, double worldY) {
        Tile tile = getTileAt(worldX, worldY);
        return tile != null && tile.isWalkable();
    }

    public MapCoord getCurrentCoord()      { return currentCoord; }
    public Map<MapCoord, Tile[][]> getLoadedMaps() { return loadedMaps; }
}
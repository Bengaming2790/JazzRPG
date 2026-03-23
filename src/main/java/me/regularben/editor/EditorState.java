package me.regularben.editor;

import me.regularben.map.util.Tile;
import me.regularben.map.util.TileType;

import java.util.ArrayDeque;
import java.util.Deque;

public class EditorState {

    public final int cols;
    public final int rows;

    private final Tile[][] tiles;
    private TileType selectedTile = null;
    private String currentFilePath = null;

    private static final int MAX_HISTORY = 10;
    private final Deque<Tile[][]> undoStack = new ArrayDeque<>();

    public EditorState(int cols, int rows) {
        this.cols  = cols;
        this.rows  = rows;
        this.tiles = new Tile[rows][cols];
    }

    public void saveSnapshot() {
        Tile[][] snapshot = new Tile[rows][cols];
        for (int r = 0; r < rows; r++)
            snapshot[r] = tiles[r].clone();

        undoStack.push(snapshot);

        if (undoStack.size() > MAX_HISTORY)
            undoStack.removeLast();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void undo() {
        if (!canUndo()) return;
        Tile[][] snapshot = undoStack.pop();
        for (int r = 0; r < rows; r++)
            tiles[r] = snapshot[r].clone();
    }


    public Tile getTile(int col, int row) {
        if (outOfBounds(col, row)) return null;
        return tiles[row][col];
    }

    public void setTile(int col, int row, TileType type) {
        if (outOfBounds(col, row)) return;
        tiles[row][col] = (type != null) ? new Tile(type, col, row) : null;
    }

    public Tile[][] getTiles() { return tiles; }


    public TileType getSelectedTile()            { return selectedTile; }
    public void setSelectedTile(TileType t)      { this.selectedTile = t; }

    public String getCurrentFilePath()           { return currentFilePath; }
    public void setCurrentFilePath(String path)  { this.currentFilePath = path; }

    public boolean outOfBounds(int col, int row) {
        return col < 0 || row < 0 || col >= cols || row >= rows;
    }
}
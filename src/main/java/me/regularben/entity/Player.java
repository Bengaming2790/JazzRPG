package me.regularben.entity;

import me.regularben.map.MapCoord;
import me.regularben.map.TileMapLoader;
import me.regularben.map.WorldMap;
import me.regularben.map.util.Tile;
import me.regularben.map.util.behaviors.DamageBehavior;
import me.regularben.util.Input;
import me.regularben.util.Location;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.io.InputStream;

public class Player extends Entity {

    Location playerLoc;
    public boolean debug;
    private final double moveSpeed = 4.0;
    private WorldMap worldMap;
    private double tileDamageSpeed = 1.0;
    public Player() {
        playerLoc    = new Location(TileMapLoader.COLS / 2.0, TileMapLoader.ROWS / 2.0);
        spriteWidth  = 16;
        spriteHeight = 16;
        loadSprite("player.png");
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }
    private void loadSprite(String path) {
        try (InputStream is = Player.class.getClassLoader()
                .getResourceAsStream("assets/textures/" + path)) {
            if (is == null) {
                System.err.println("[Player] Sprite not found: " + path);
                return;
            }
            sprite = ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("[Player] Failed to load sprite: " + path);
            e.printStackTrace();
        }
    }

    public double getX() { return playerLoc.getX(); }
    public double getY() { return playerLoc.getY(); }

    public void update() {
    	double x = playerLoc.getX();
    	double y = playerLoc.getY();
    	double newX = x, newY = y;

    	double dx = 0;
    	double dy = 0;

    	if (Input.isKeyDown(KeyEvent.VK_W) || Input.isKeyDown(KeyEvent.VK_UP))    dy -= 1.0;
    	if (Input.isKeyDown(KeyEvent.VK_S) || Input.isKeyDown(KeyEvent.VK_DOWN))  dy += 1.0;
    	if (Input.isKeyDown(KeyEvent.VK_A) || Input.isKeyDown(KeyEvent.VK_LEFT))  dx -= 1.0;
    	if (Input.isKeyDown(KeyEvent.VK_D) || Input.isKeyDown(KeyEvent.VK_RIGHT)) dx += 1.0;

    
    	if (dx != 0 && dy != 0) {
    	    double length = Math.sqrt(dx * dx + dy * dy);
    	    dx /= length;
    	    dy /= length;
    	}

    	double currentMoveSpeed = moveSpeed / 16.0;
    	newX += dx * currentMoveSpeed;
    	newY += dy * currentMoveSpeed;

    	if (canMoveTo(newX, y)) x = newX;
    	if (canMoveTo(x, newY)) y = newY;

    	playerLoc.setLocal(x, y);

        if (worldMap != null) {
            MapCoord current = worldMap.coordAt(x, y);
            if (!current.equals(worldMap.getCurrentCoord())) {
                worldMap.updateLoadedMaps(current);
            }
        }
        
        if (shouldDamagePlayer(getStandingTile())) {
        	System.out.println("[Debug] damageing player by " + tileDamageSpeed);
        	this.damage(tileDamageSpeed);
        }

        if (debug && Input.isKeyReleased(KeyEvent.VK_L))
            System.out.println("World pos: " + x + ", " + y);
    }

    public Tile getStandingTile() {
    	return worldMap.getTileAt(playerLoc.getX(), playerLoc.getY());
    }
    
    public boolean shouldDamagePlayer(Tile tile) {
        return tile.getType().getBehaviors().stream()
                   .anyMatch(behavior -> behavior instanceof DamageBehavior);
    }

    
    private boolean canMoveTo(double x, double y) {
        if (worldMap == null) return true;

        double inset  = 0.1;
        double left   = x       + inset;
        double right  = x + 1.0 - inset;
        double top    = y       + inset;
        double bottom = y + 1.0 - inset;

        return worldMap.canMoveTo(left,  top)
                && worldMap.canMoveTo(right, top)
                && worldMap.canMoveTo(left,  bottom)
                && worldMap.canMoveTo(right, bottom);
    }
}

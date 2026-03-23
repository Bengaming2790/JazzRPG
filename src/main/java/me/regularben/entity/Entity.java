package me.regularben.entity;

import me.regularben.util.Location;

import java.awt.image.BufferedImage;

public class Entity {

    Location location;
    private double hp;
    private boolean isAlive;

    protected BufferedImage sprite;
    protected int spriteWidth  = 16;
    protected int spriteHeight = 16;

    public void damage(int hp) {
        this.hp -= hp;
        if (hp <= 0) {
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public double[] getLocation() {
        return location.getLocal();
    }

    public BufferedImage getSprite()   { return sprite; }
    public int getSpriteWidth()        { return spriteWidth; }
    public int getSpriteHeight()       { return spriteHeight; }
}
package me.regularben.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class Input extends KeyAdapter {

    private static final Set<Integer> keysDown = new HashSet<>();
    private static final Set<Integer> keysReleased = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        keysDown.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keysDown.remove(key);
        keysReleased.add(key);
    }

    public static boolean isKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }

    public static boolean isKeyReleased(int keyCode) {
        return keysReleased.contains(keyCode);
    }

    public static void endFrame() {
        keysReleased.clear();
    }
}
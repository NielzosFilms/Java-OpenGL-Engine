package com.nielzosfilms.engine;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyHandler {
    private static final KeyHandler instance = new KeyHandler();

    private final ArrayList<KeyEventListener> listeners = new ArrayList<>();

    private final HashMap<Integer, Boolean> keysPressed = new HashMap<>();

    private KeyHandler() {
    }

    public static KeyHandler getInstance() {
        return instance;
    }

    public void addEventListener(KeyEventListener listener) {
        listeners.add(listener);
    }

    public void keyCallback(Long window, Integer key, Integer scancode, Integer action, Integer mods) {
        if (action == GLFW_PRESS) {
            keysPressed.put(key, true);
            listeners.forEach(listener -> listener.onKeyDown(key));
        } else if (action == GLFW_RELEASE) {
            keysPressed.put(key, false);
            listeners.forEach(listener -> listener.onKeyUp(key));
        }
    }

    public Boolean isKeyPressed(Integer keyCode) {
        if (!keysPressed.containsKey(keyCode)) {
            return false;
        }
        return keysPressed.get(keyCode);
    }
}

package com.nielzosfilms.engine;


import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseHandler {
    private static final MouseHandler instance = new MouseHandler();

    private final ArrayList<MouseEventListener> listeners = new ArrayList<>();

    private Double scrollX = 0.0, scrollY = 0.0;
    private Double posX = 0.0, posY = 0.0;
    private Double lastX = 0.0, lastY = 0.0;
    private Boolean isDragging = false;
    private HashMap<Integer, Boolean> mouseButtonsPressed = new HashMap<>();

    private MouseHandler() {
    }

    public static MouseHandler getInstance() {
        return instance;
    }

    public void addEventListener(MouseEventListener listener) {
        listeners.add(listener);
    }

    public void mousePosCallback(Long window, Double posX, Double posY) {
        lastX = this.posX;
        lastY = this.posY;

        this.posX = posX;
        this.posY = posY;

        isDragging = isMouseButtonDown(GLFW_MOUSE_BUTTON_1) || isMouseButtonDown(GLFW_MOUSE_BUTTON_2) || isMouseButtonDown(GLFW_MOUSE_BUTTON_3);
    }

    public void mouseScrollCallback(Long window, Double xOffset, Double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public void mouseButtonCallback(Long window, Integer button, Integer action, Integer mods) {
        if (action == GLFW_PRESS) {
            mouseButtonsPressed.put(button, true);
            listeners.forEach(listener -> listener.onMouseDown(button));
        } else if (action == GLFW_RELEASE) {
            mouseButtonsPressed.put(button, false);
            listeners.forEach(listener -> listener.onMouseUp(button));
        }
    }

    public Boolean isMouseButtonDown(Integer button) {
        if (!mouseButtonsPressed.containsKey(button)) {
            return false;
        }
        return mouseButtonsPressed.get(button);
    }


}

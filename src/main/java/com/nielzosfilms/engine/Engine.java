package com.nielzosfilms.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {
    private static final Logger log = LogManager.getLogger(Engine.class);

    private long window;

    private KeyHandler keyHandler = KeyHandler.getInstance();
    private MouseHandler mouseHandler = MouseHandler.getInstance();

    public void run() {
        init();
        loop();

        destroy();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize glfw");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "Engine", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create glfw window");
        }

        glfwSetKeyCallback(window, keyHandler::keyCallback);
        glfwSetCursorPosCallback(window, mouseHandler::mousePosCallback);
        glfwSetMouseButtonCallback(window, mouseHandler::mouseButtonCallback);
        glfwSetScrollCallback(window, mouseHandler::mouseScrollCallback);

        keyHandler.addEventListener(new KeyEventListener() {
            @Override
            public void onKeyDown(Integer key) {
                if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(window, true);
            }

            @Override
            public void onKeyUp(Integer key) {
            }
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidmode != null;
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    private void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}

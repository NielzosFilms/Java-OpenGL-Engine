package com.nielzosfilms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Version;

public class Game {
    private static final Logger log = LogManager.getLogger(Game.class);

    public static void main(String[] args) {
        log.info("Running with LWJGL@" + Version.getVersion());

        Engine engine = new Engine();
        engine.run();
    }
}

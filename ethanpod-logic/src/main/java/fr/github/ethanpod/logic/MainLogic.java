package fr.github.ethanpod.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainLogic {
    private static final Logger logger = LogManager.getLogger(MainLogic.class);

    public static void main(String[] args) {
        logger.info("Start Logic");
        start();
        logger.info("Close Logic");
    }

    public static void start() {
        logger.info("logic");
    }
}

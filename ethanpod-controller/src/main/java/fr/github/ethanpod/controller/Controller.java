package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.service.AsyncServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Controller {
    protected static final Logger logger = LogManager.getLogger(Controller.class);
    protected final MessageRouter messageRouter = MessageRouter.getInstance();
    protected AsyncServiceManager asyncServiceManager;

    protected Controller(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
    }

    abstract void initializeUI();
}

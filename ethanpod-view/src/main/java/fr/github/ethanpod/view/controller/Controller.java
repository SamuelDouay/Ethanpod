package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.controller.event.UIEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Controller {
    protected static final Logger logger = LogManager.getLogger(Controller.class);
    protected final UIEventManager eventManager;
    protected AsyncServiceManager asyncServiceManager;

    protected Controller(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
        this.eventManager = UIEventManager.getInstance();
    }

    abstract void initializeUI();
}

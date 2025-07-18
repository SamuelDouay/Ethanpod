package fr.github.ethanpod.view.thread.controller;

import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.layout.NavigationContainer;
import fr.github.ethanpod.view.thread.callback.UIUpdateCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Controller {
    protected static final Logger logger = LogManager.getLogger(Controller.class);
    protected UIUpdateCallback uiUpdateCallback;
    protected AsyncServiceManager asyncServiceManager;

    public Controller(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
    }

    public void setNavigationContainer(NavigationContainer navigationContainer) {
        this.uiUpdateCallback = navigationContainer;
        logger.info("🟢 NavigationContainer configuré dans ViewThread");
    }

    abstract void initializeUI();
}

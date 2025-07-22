package fr.github.ethanpod.controller;

import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.util.manager.BaseServiceManager;
import fr.github.ethanpod.util.manager.ServiceConstants;

public class ControllerManager extends BaseServiceManager<Controller> {
    private final AsyncServiceManager asyncServiceManager;

    public ControllerManager(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
        initializeServices();
    }

    private void initializeServices() {
        registerService(ServiceConstants.NAVIGATION_SERVICE, new NavigationController(asyncServiceManager));
        registerService(ServiceConstants.INBOX_SERVICE, new InboxController(asyncServiceManager));
    }

    public NavigationController getNavigationService() {
        return getService(ServiceConstants.NAVIGATION_SERVICE, NavigationController.class);
    }

    public InboxController getInboxService() {
        return getService(ServiceConstants.INBOX_SERVICE, InboxController.class);
    }

    public void initializeAllServices() {
        getAllServices().forEach(Controller::initializeUI);
    }
}

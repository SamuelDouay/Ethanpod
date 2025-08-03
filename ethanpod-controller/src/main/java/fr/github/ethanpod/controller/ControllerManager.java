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
        registerService(ServiceConstants.QUEUE_SERVICE, new QueueController(asyncServiceManager));
        registerService(ServiceConstants.PODCAST_SERVICE, new PodcastController(asyncServiceManager));
    }

    public void initializeAllServices() {
        getAllServices().forEach(Controller::initializeUI);
    }
}

package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.util.manager.BaseServiceManager;
import fr.github.ethanpod.util.manager.ServiceConstants;

public class UIControllerManager extends BaseServiceManager<UIController> {

    public UIControllerManager() {
        initializeServices();
    }

    private void initializeServices() {
        registerService(ServiceConstants.NAVIGATION_SERVICE, new NavigationUIController());
        registerService(ServiceConstants.INBOX_SERVICE, new InboxUIController());
        registerService(ServiceConstants.QUEUE_SERVICE, new QueueUIController());
    }

    public NavigationUIController getNavigationService() {
        return getService(ServiceConstants.NAVIGATION_SERVICE, NavigationUIController.class);
    }

    public InboxUIController getInboxService() {
        return getService(ServiceConstants.INBOX_SERVICE, InboxUIController.class);
    }

    public QueueUIController getQueueService() {
        return getService(ServiceConstants.QUEUE_SERVICE, QueueUIController.class);
    }
}
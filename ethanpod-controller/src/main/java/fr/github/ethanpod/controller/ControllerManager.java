package fr.github.ethanpod.controller;

import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private final Map<String, Controller> services = new HashMap<>();
    private final AsyncServiceManager asyncServiceManager;


    public ControllerManager(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
        initializeServices();
    }

    private void initializeServices() {
        // Enregistrer les services disponibles
        registerService("navigation", new NavigationController(asyncServiceManager));
        registerService("inbox", new InboxController(asyncServiceManager));
    }

    public void registerService(String serviceId, Controller service) {
        services.put(serviceId, service);
    }

    public NavigationController getNavigationService() {
        return (NavigationController) services.get("navigation");
    }

    public InboxController getInboxService() {
        return (InboxController) services.get("inbox");
    }

    public void initializeAllServices() {
        services.values().forEach(Controller::initializeUI);
    }
}

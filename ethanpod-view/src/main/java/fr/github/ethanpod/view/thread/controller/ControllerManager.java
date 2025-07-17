package fr.github.ethanpod.view.thread.controller;

import fr.github.ethanpod.view.layout.NavigationContainer;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private final Map<String, Controller> services = new HashMap<>();

    public ControllerManager() {
        initializeServices();
    }

    private void initializeServices() {
        // Enregistrer les services disponibles
        registerService("navigation", new NavigationController());
        registerService("inbox", new InboxController());
    }

    public void setNavigationContainer(NavigationContainer navigationContainer) {
        services.values().forEach(service -> service.setNavigationContainer(navigationContainer));
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

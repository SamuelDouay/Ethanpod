package fr.github.ethanpod.view.controller;

import java.util.HashMap;
import java.util.Map;

public class UIControllerManager {
    private final Map<String, UIController> services = new HashMap<>();

    public UIControllerManager() {
        initializeServices();
    }

    private void initializeServices() {
        // Enregistrer les services disponibles
        registerService("navigation", new NavigationUIController());
        registerService("inbox", new InboxUIController());
    }

    public void registerService(String serviceId, UIController service) {
        services.put(serviceId, service);
    }

    public NavigationUIController getNavigationService() {
        return (NavigationUIController) services.get("navigation");
    }

    public InboxUIController getInboxService() {
        return (InboxUIController) services.get("inbox");
    }
}

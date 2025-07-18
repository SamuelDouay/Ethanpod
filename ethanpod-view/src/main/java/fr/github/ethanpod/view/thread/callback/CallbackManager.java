package fr.github.ethanpod.view.thread.callback;

import java.util.HashMap;
import java.util.Map;

public class CallbackManager {
    private final Map<String, CallBack> services = new HashMap<>();

    public CallbackManager() {
        initializeServices();
    }

    private void initializeServices() {
        // Enregistrer les services disponibles
        registerService("navigation", new NavigationCallBack());
        registerService("inbox", new InboxCallBack());
    }

    public void registerService(String serviceId, CallBack service) {
        services.put(serviceId, service);
    }

    public NavigationCallBack getNavigationService() {
        return (NavigationCallBack) services.get("navigation");
    }

    public InboxCallBack getInboxService() {
        return (InboxCallBack) services.get("inbox");
    }

    public void initializeAllServices() {
        //services.values().forEach(Controller::initializeUI);
    }
}

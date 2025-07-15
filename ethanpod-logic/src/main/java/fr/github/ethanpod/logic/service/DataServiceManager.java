package fr.github.ethanpod.logic.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class DataServiceManager {
    private static final Logger logger = LogManager.getLogger(DataServiceManager.class);
    private final Map<String, DataService> services = new HashMap<>();
    private final ExecutorService executor;

    public DataServiceManager(ExecutorService executor) {
        this.executor = executor;
        initializeServices();
    }

    private void initializeServices() {
        // Enregistrer les services disponibles
        registerService("navigation", new DataNavigationService(executor));
        registerService("inbox", new DataInboxService(executor));
    }

    public void registerService(String serviceId, DataService service) {
        services.put(serviceId, service);
    }

    public DataNavigationService getNavigationService() {
        return (DataNavigationService) services.get("navigation");
    }

    public DataInboxService getInboxService() {
        return (DataInboxService) services.get("inbox");
    }

    public void refreshAllData() {
        logger.info("Rafraîchissement de toutes les données des services");
        services.values().forEach(service -> {
            try {
                service.refreshData();
            } catch (Exception e) {
                logger.error("Erreur lors du rafraîchissement du service", e);
            }
        });
    }
}
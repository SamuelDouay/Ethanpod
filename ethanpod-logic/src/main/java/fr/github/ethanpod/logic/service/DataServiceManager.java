package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.util.manager.BaseServiceManager;
import fr.github.ethanpod.util.manager.ServiceConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class DataServiceManager extends BaseServiceManager<DataService> {
    private static final Logger logger = LogManager.getLogger(DataServiceManager.class);
    private final ExecutorService executor;

    public DataServiceManager(ExecutorService executor) {
        this.executor = executor;
        initializeServices();
    }

    private void initializeServices() {
        registerService(ServiceConstants.NAVIGATION_SERVICE, new DataNavigationService(executor));
        registerService(ServiceConstants.INBOX_SERVICE, new DataInboxService(executor));
    }

    public DataNavigationService getNavigationService() {
        return getService(ServiceConstants.NAVIGATION_SERVICE, DataNavigationService.class);
    }

    public DataInboxService getInboxService() {
        return getService(ServiceConstants.INBOX_SERVICE, DataInboxService.class);
    }

    public void refreshAllData() {
        logger.info("Rafraîchissement de toutes les données des services");
        getAllServices().forEach(service -> {
            try {
                service.refreshData();
            } catch (Exception e) {
                logger.error("Erreur lors du rafraîchissement du service", e);
            }
        });
    }

}
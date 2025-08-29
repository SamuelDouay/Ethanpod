package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.exception.EthanpodRuntimeException;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.manager.BaseServiceManager;
import fr.github.ethanpod.util.manager.ServiceConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class DataServiceManager extends BaseServiceManager<DataService> {
    private static final Logger logger = LogManager.getLogger(DataServiceManager.class);
    private final ExecutorService executor;

    public DataServiceManager(ExecutorService executor, DatabaseManager databaseManager) {
        this.executor = executor;
        initializeServices(databaseManager);
    }

    private void initializeServices(DatabaseManager databaseManager) {
        registerService(ServiceConstants.NAVIGATION_SERVICE, new DataNavigationService(executor, databaseManager));
        registerService(ServiceConstants.INBOX_SERVICE, new DataInboxService(executor, databaseManager));
        registerService(ServiceConstants.QUEUE_SERVICE, new DataQueueService(executor, databaseManager));
        registerService(ServiceConstants.PODCAST_SERVICE, new DataPodcastService(executor, databaseManager));
        registerService(ServiceConstants.DOWNLOAD_SERVICE, new DataDownloadService(executor, databaseManager));
        registerService(ServiceConstants.EPISODE_SERVICE, new DataEpisodeService(executor, databaseManager));
        registerService(ServiceConstants.HISTORY_SERVICE, new DataHistoryService(executor, databaseManager));
    }

    public DataNavigationService getNavigationService() {
        return getService(ServiceConstants.NAVIGATION_SERVICE, DataNavigationService.class);
    }

    public DataInboxService getInboxService() {
        return getService(ServiceConstants.INBOX_SERVICE, DataInboxService.class);
    }

    public DataQueueService getQueueService() {
        return getService(ServiceConstants.QUEUE_SERVICE, DataQueueService.class);
    }

    public DataPodcastService getPodcastService() {
        return getService(ServiceConstants.PODCAST_SERVICE, DataPodcastService.class);
    }

    public DataDownloadService getDownloadService() {
        return getService(ServiceConstants.DOWNLOAD_SERVICE, DataDownloadService.class);
    }

    public DataEpisodeService getEpisodeService() {
        return getService(ServiceConstants.EPISODE_SERVICE, DataEpisodeService.class);
    }

    public DataHistoryService getHistoryService() {
        return getService(ServiceConstants.HISTORY_SERVICE, DataHistoryService.class);
    }


    public void refreshAllData() {
        logger.info("Rafraîchissement de toutes les données des services");
        getAllServices().forEach(service -> {
            try {
                service.refreshData();
            } catch (Exception e) {
                logger.error("Erreur lors du rafraîchissement du service", e);
                throw new EthanpodRuntimeException("Erreur lors du rafraîchissement du service", e.getMessage());
            }
        });
    }

}
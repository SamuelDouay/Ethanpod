package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.util.manager.BaseServiceManager;
import fr.github.ethanpod.util.manager.ServiceConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncServiceManager extends BaseServiceManager<AsyncService> {
    private static final Logger logger = LogManager.getLogger(AsyncServiceManager.class);
    private static final Pattern SERVICE_ID_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");

    public AsyncServiceManager() {
        initializeServices();
    }

    private void initializeServices() {
        registerService(ServiceConstants.NAVIGATION_SERVICE, new AsyncNavigationService());
        registerService(ServiceConstants.INBOX_SERVICE, new AsyncInboxService());
        registerService(ServiceConstants.QUEUE_SERVICE, new AsyncQueueService());
        registerService(ServiceConstants.PODCAST_SERVICE, new AsyncPodcastService());
    }

    public AsyncNavigationService getNavigationService() {
        return getService(ServiceConstants.NAVIGATION_SERVICE, AsyncNavigationService.class);
    }

    public AsyncInboxService getInboxService() {
        return getService(ServiceConstants.INBOX_SERVICE, AsyncInboxService.class);
    }

    public AsyncQueueService getQueueService() {
        return getService(ServiceConstants.QUEUE_SERVICE, AsyncQueueService.class);
    }

    public AsyncPodcastService getPodcastService() {
        return getService(ServiceConstants.PODCAST_SERVICE, AsyncPodcastService.class);
    }

    public void handleResponse(ThreadMessage message) {
        ServiceConstants serviceId = extractServiceId(message);
        AsyncService service = services.get(serviceId);

        if (service != null) {
            service.handleResponse(message);
        } else {
            logger.warn("Aucun service trouvé pour: {}", serviceId);
        }
    }

    private ServiceConstants extractServiceId(ThreadMessage message) {
        String requestId = message.getId();
        if (requestId != null) {
            Matcher matcher = SERVICE_ID_PATTERN.matcher(requestId);
            if (matcher.find()) {
                String serviceIdString = matcher.group(1).toLowerCase();
                return ServiceConstants.fromName(serviceIdString);
            }
        }
        throw new IllegalArgumentException("Invalid requestId format: " + requestId);
    }

    public void refreshAllData() {
        logger.debug("Rafraîchissement de toutes les données des services");
        performOperationOnAllServices("refresh", AsyncService::refreshData);
    }

    public void initializeAllServices() {
        logger.debug("Initialisation de tous les services");
        performOperationOnAllServices("initialization", AsyncService::initialize);
    }

    public void stopAllServices() {
        logger.debug("Arrêt de tous les services");
        performOperationOnAllServices("stop", AsyncService::stop);
    }

    private void performOperationOnAllServices(String operationName, ServiceOperation operation) {
        getAllServices().forEach(service -> {
            try {
                operation.execute(service);
            } catch (Exception _) {
                logger.error("Erreur lors de {} du service", operationName);
            }
        });
    }

    @FunctionalInterface
    private interface ServiceOperation {
        void execute(AsyncService service) throws Exception;
    }
}
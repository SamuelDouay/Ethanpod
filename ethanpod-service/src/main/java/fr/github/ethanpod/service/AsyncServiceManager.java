package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.exception.EthanpodRuntimeException;
import fr.github.ethanpod.exception.thread.ThreadCommunicationException;
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
        registerService(ServiceConstants.DOWNLOAD_SERVICE, new AsyncDownloadService());
        registerService(ServiceConstants.EPISODE_SERVICE, new AsyncEpisodeDataService());
        registerService(ServiceConstants.HISTORY_SERVICE, new AsyncHistoryService());
        registerService(ServiceConstants.SURPRISE_SERVICE, new AsyncSurpriseService());
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

    public AsyncDownloadService getDownloadService() {
        return getService(ServiceConstants.DOWNLOAD_SERVICE, AsyncDownloadService.class);
    }

    public AsyncEpisodeDataService getEpisodeService() {
        return getService(ServiceConstants.EPISODE_SERVICE, AsyncEpisodeDataService.class);
    }

    public AsyncHistoryService getHistoryService() {
        return getService(ServiceConstants.HISTORY_SERVICE, AsyncHistoryService.class);
    }

    public AsyncSurpriseService getSurpriseService() {
        return getService(ServiceConstants.SURPRISE_SERVICE, AsyncSurpriseService.class);
    }

    public void handleResponse(ThreadMessage message) {
        try {
            ServiceConstants serviceId = extractServiceId(message);
            AsyncService service = services.get(serviceId);

            if (service != null) {
                service.handleResponse(message);
            } else {
                assert serviceId != null;
                throw ThreadCommunicationException.messageRoutingFailed(
                        "AsyncServiceManager",
                        serviceId.name(),
                        message.id()
                );
            }
        } catch (ThreadCommunicationException e) {
            logger.error("Erreur de communication inter-thread: {}", e.getMessage(), e);
        } catch (Exception e) {
            throw EthanpodRuntimeException.systemError(
                    "Erreur inattendue lors du traitement de la réponse",
                    e
            );
        }
    }

    private ServiceConstants extractServiceId(ThreadMessage message) throws ThreadCommunicationException {
        String requestId = message.id();
        if (requestId != null) {
            Matcher matcher = SERVICE_ID_PATTERN.matcher(requestId);
            if (matcher.find()) {
                String serviceIdString = matcher.group(1).toLowerCase();
                return ServiceConstants.fromName(serviceIdString);
            } else {
                throw ThreadCommunicationException.messageServiceUnknow(requestId);
            }
        }
        return null;
    }

    public void refreshAllData() {
        logger.debug("Rafraîchissement de toutes les données des services");
        performOperationOnAllServices("rafraîchissement", AsyncService::refreshData);
    }

    public void initializeAllServices() {
        logger.debug("Initialisation de tous les services");
        performOperationOnAllServices("initialisation", AsyncService::initialize);
    }

    public void stopAllServices() {
        logger.debug("Arrêt de tous les services");
        performOperationOnAllServices("arrêt", AsyncService::stop);
    }

    private void performOperationOnAllServices(String operationName, ServiceOperation operation) {
        getAllServices().forEach(service -> {
            try {
                operation.execute(service);
            } catch (ThreadCommunicationException e) {
                logger.error("Erreur de communication lors de {} du service {}", operationName, service.getClass().getSimpleName(), e);
            } catch (Exception e) {
                throw EthanpodRuntimeException.systemError(
                        "Erreur inattendue lors de " + operationName + " du service " + service.getClass().getSimpleName(),
                        e
                );
            }
        });
    }

    @FunctionalInterface
    private interface ServiceOperation {
        void execute(AsyncService service) throws ThreadCommunicationException;
    }
}

package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceManager {
    private static final Logger logger = LogManager.getLogger(ServiceManager.class);
    private final Map<String, AsyncService> services = new HashMap<>();

    public ServiceManager() {
        initializeServices();
    }

    private void initializeServices() {
        // Enregistrer les services disponibles
        registerService("navigation", new AsyncNavigationService());
        registerService("inbox", new AsyncInboxService());
    }

    public void registerService(String serviceId, AsyncService service) {
        services.put(serviceId, service);
        logger.info("Service '{}' enregistré", serviceId);
    }

    public AsyncNavigationService getNavigationService() {
        return (AsyncNavigationService) services.get("navigation");
    }

    public AsyncInboxService getInboxService() {
        return (AsyncInboxService) services.get("inbox");
    }

    public void handleResponse(ThreadMessage message) {
        String serviceId = extractServiceId(message);
        AsyncService service = services.get(serviceId);

        if (service != null) {
            service.handleResponse(message);
        } else {
            logger.warn("Aucun service trouvé pour: {}", serviceId);
        }
    }

    private String extractServiceId(ThreadMessage message) {
        String requestId = message.getRequestId();
        if (requestId != null) {
            Matcher matcher = Pattern.compile("\\[([^\\]]+)\\]").matcher(requestId);
            if (matcher.find()) {
                return matcher.group(1).toLowerCase();
            }
        }
        throw new IllegalArgumentException("Invalid requestId format: " + requestId);
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

    public void initializeAllServices() {
        logger.info("Initialisation de tous les services");
        services.values().forEach(service -> {
            try {
                service.initialize();
            } catch (Exception e) {
                logger.error("Erreur lors de l'initialisation du service", e);
            }
        });
    }

    public void stopAllServices() {
        logger.info("Arrêt de tous les services");
        services.values().forEach(service -> {
            try {
                service.stop();
            } catch (Exception e) {
                logger.error("Erreur lors de l'arrêt du service", e);
            }
        });
    }
}
package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire central des services asynchrones
 * Permet d'ajouter et de gérer différents types de services
 */
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
        // Ajouter d'autres services ici facilement
        // registerService("user", new AsyncUserService());
        // registerService("settings", new AsyncSettingsService());
    }

    public void registerService(String serviceId, AsyncService service) {
        services.put(serviceId, service);
        logger.info("Service '{}' enregistré", serviceId);
    }

    public AsyncService getService(String serviceId) {
        return services.get(serviceId);
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
        String content = message.getContent();

        // Logique pour déterminer quel service doit traiter le message
        if (content.startsWith("NAV_") || content.contains("NAVIGATION")) {
            return "navigation";
        } else if (content.startsWith("INBOX_") || content.contains("INBOX")) {
            return "inbox";
        }

        return "unknown";
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
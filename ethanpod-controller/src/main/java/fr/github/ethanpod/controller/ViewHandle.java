package fr.github.ethanpod.controller;


import fr.github.ethanpod.core.thread.MessageCategory;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.NotificationType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ViewHandle {
    private static final Logger logger = LogManager.getLogger(ViewHandle.class);
    private final AsyncServiceManager asyncServiceManager;
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final ControllerManager controllerManager;
    private boolean shutdownRequested = false;
    private boolean processingInterrupted = false;

    public ViewHandle() {
        this.asyncServiceManager = new AsyncServiceManager();
        this.messageQueue = MessageRouter.getInstance().registerThread("ViewThread");
        this.controllerManager = new ControllerManager(asyncServiceManager);
    }

    public void stopAllService() {
        logger.debug("Arrêt de tous les services ViewHandle");
        shutdownRequested = true;  // Ajouter cette ligne
        this.asyncServiceManager.stopAllServices();
    }

    public void processIncomingMessages() throws InterruptedException {
        if (processingInterrupted || shutdownRequested) {
            logger.debug("Arrêt en cours");
            return;
        }
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.debug(message);
            switch (message.getCategory()) {
                case MessageCategory.RESPONSE -> handleResponse(message);
                case MessageCategory.DATA_UPDATE -> handleDataUpdate(message);
                case MessageCategory.NOTIFICATION -> handleNotification(message);
                case MessageCategory.ERROR -> handleError(message);
                default -> logger.warn("Type de message non géré: {}", message.getType());
            }
        }
    }

    private void handleResponse(ThreadMessage message) {
        asyncServiceManager.handleResponse(message);
    }

    private void handleDataUpdate(ThreadMessage message) {
        if (shutdownRequested) {
            logger.debug("Mise à jour ignorée (arrêt en cours): {}", message.getType());
            return;
        }
        String content = message.getType().toString();

        switch (content) {
            case "DATA_REFRESHED" -> logger.info("data refresh");
            case "INBOX_UPDATED" -> logger.info("Inbox update");
            default -> logger.warn("Contenu de mise à jour non géré: {}", content);
        }
    }

    private void handleNotification(ThreadMessage message) {
        if (shutdownRequested) {
            logger.debug("Notification ignorée (arrêt en cours): {}", message.getType());
            return;
        }
        logger.debug("Notification reçue: {}", message.getType());

        NotificationType eventType = (NotificationType) message.getType();

        switch (eventType) {
            case NotificationType.JAVAFX_READY -> {
                logger.debug("Javafx ready");
                controllerManager.initializeAllServices();
            }
            case NotificationType.LOGIC_READY -> {
                asyncServiceManager.initializeAllServices();
                logger.debug("Logic ready");
            }
            case NotificationType.UI_EVENT_READY -> logger.debug("Event ready");
            default -> throw new IllegalStateException("Unexpected value: " + message.getType());
        }
    }

    private void handleError(ThreadMessage message) {
        logger.error("Erreur reçue du thread de logique: {}", message.getType());
    }

    public void interruptProcessing() {
        logger.debug("Interruption du traitement des messages demandée");
        processingInterrupted = true;
    }

    public void flushPendingMessages() {
        logger.debug("Traitement des messages restants...");
        int processedCount = 0;

        try {
            while (!messageQueue.isEmpty() && processedCount < 10) { // Limite de sécurité
                ThreadMessage message = messageQueue.poll();
                if (message != null) {
                    logger.debug("Message final: {}", message.getType());

                    // Traiter seulement les responses critiques
                    if (message.getCategory() == MessageCategory.RESPONSE) {
                        handleResponse(message);
                    }
                    processedCount++;
                }
            }
            logger.debug("{} messages restants traités", processedCount);
        } catch (Exception e) {
            logger.warn("Erreur lors du flush: {}", e.getMessage());
        }
    }

}

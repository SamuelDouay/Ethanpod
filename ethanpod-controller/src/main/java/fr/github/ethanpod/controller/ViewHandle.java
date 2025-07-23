package fr.github.ethanpod.controller;


import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
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
        logger.info("🟢 Arrêt de tous les services ViewHandle");
        shutdownRequested = true;  // Ajouter cette ligne
        this.asyncServiceManager.stopAllServices();
    }

    public void processIncomingMessages() throws InterruptedException {
        if (processingInterrupted || shutdownRequested) {
            return; // Sortir immédiatement si arrêt demandé
        }
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.info("🟢 {}", message);

            if (processingInterrupted || shutdownRequested) {
                logger.info("🟢 Message ignoré (arrêt en cours): {}", message.getContent());
                return;
            }

            switch (message.getType()) {
                case MessageType.RESPONSE -> handleResponse(message);
                case MessageType.DATA_UPDATE -> handleDataUpdate(message);
                case MessageType.NOTIFICATION -> handleNotification(message);
                case MessageType.ERROR -> handleError(message);
                default -> logger.warn("Type de message non géré: {}", message.getType());
            }
        }
    }

    private void handleResponse(ThreadMessage message) {
        asyncServiceManager.handleResponse(message);
    }

    private void handleDataUpdate(ThreadMessage message) {
        if (shutdownRequested) {
            logger.debug("🟠 Mise à jour ignorée (arrêt en cours): {}", message.getContent());
            return;
        }
        String content = message.getContent();

        switch (content) {
            case "DATA_REFRESHED" -> logger.info("data refresh");
            case "INBOX_UPDATED" -> logger.info("Inbox update");
            default -> logger.warn("Contenu de mise à jour non géré: {}", content);
        }
    }

    private void handleNotification(ThreadMessage message) {
        if (shutdownRequested) {
            logger.debug("🟠 Notification ignorée (arrêt en cours): {}", message.getContent());
            return;
        }
        logger.info("🟢 Notification reçue: {}", message.getContent());

        if ("LOGIC_READY".equals(message.getContent())) {
            asyncServiceManager.initializeAllServices();
            logger.info("🟢 Logic ready");
        }
        if ("UI_EVENT_READY".equals(message.getContent())) {
            logger.info("🟢 Event ready");

        }
        if ("JAVAFX_READY".equals(message.getContent())) {
            controllerManager.initializeAllServices();
        }
    }

    private void handleError(ThreadMessage message) {
        logger.error("🔴 Erreur reçue du thread de logique: {}", message.getContent());
    }

    public void interruptProcessing() {
        logger.info("🟢 Interruption du traitement des messages demandée");
        processingInterrupted = true;
    }

    public void flushPendingMessages() {
        logger.info("🟢 Traitement des messages restants...");
        int processedCount = 0;

        try {
            while (!messageQueue.isEmpty() && processedCount < 10) { // Limite de sécurité
                ThreadMessage message = messageQueue.poll();
                if (message != null) {
                    logger.info("🟢 Message final: {}", message.getContent());

                    // Traiter seulement les responses critiques
                    if (message.getType() == MessageType.RESPONSE) {
                        handleResponse(message);
                    }
                    processedCount++;
                }
            }
            logger.info("🟢 {} messages restants traités", processedCount);
        } catch (Exception e) {
            logger.warn("🟠 Erreur lors du flush: {}", e.getMessage());
        }
    }

}

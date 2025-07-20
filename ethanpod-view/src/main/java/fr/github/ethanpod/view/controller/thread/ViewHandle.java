package fr.github.ethanpod.view.controller.thread;


import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.controller.ControllerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ViewHandle {
    private static final Logger logger = LogManager.getLogger(ViewHandle.class);
    private final AsyncServiceManager asyncServiceManager;
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final ControllerManager controllerManager;

    public ViewHandle() {
        this.asyncServiceManager = new AsyncServiceManager();
        this.messageQueue = MessageRouter.getInstance().registerThread("ViewThread");
        this.controllerManager = new ControllerManager(asyncServiceManager);
    }

    public void stopAllService() {
        this.asyncServiceManager.stopAllServices();
    }

    public void processIncomingMessages() throws InterruptedException {
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.info("🟢 {}", message);

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
        String content = message.getContent();

        switch (content) {
            case "DATA_REFRESHED" -> {
                logger.info("data refresh");
            }
            case "INBOX_UPDATED" -> {
                logger.info("Inbox update");
            }
            default -> logger.warn("Contenu de mise à jour non géré: {}", content);
        }
    }

    private void handleNotification(ThreadMessage message) {
        logger.info("🟢 Notification reçue: {}", message.getContent());

        if ("LOGIC_READY".equals(message.getContent())) {
            asyncServiceManager.initializeAllServices();
            logger.info("🟢 Services initialisés, en attente de JavaFX");
        }
        if ("JAVAFX_READY".equals(message.getContent())) {
            controllerManager.initializeAllServices();
        }
    }

    private void handleError(ThreadMessage message) {
        logger.error("🔴 Erreur reçue du thread de logique: {}", message.getContent());
    }

}

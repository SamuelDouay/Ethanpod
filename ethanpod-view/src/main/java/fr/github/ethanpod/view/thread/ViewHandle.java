package fr.github.ethanpod.view.thread;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.thread.controller.ControllerManager;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ViewHandle {
    private static final Logger logger = LogManager.getLogger(ViewHandle.class);
    private final AsyncServiceManager asyncServiceManager;
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final ControllerManager controllerManager;
    private UIUpdateCallback uiUpdateCallback;

    public ViewHandle() {
        this.asyncServiceManager = new AsyncServiceManager();
        this.messageQueue = MessageRouter.getInstance().registerThread("ViewThread");
        this.controllerManager = new ControllerManager();
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public void stopAllService() {
        this.asyncServiceManager.stopAllServices();
    }

    public void processIncomingMessages() throws InterruptedException {
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.info("🟢 {}", message);

            switch (message.getType()) {
                case RESPONSE -> handleResponse(message);
                case DATA_UPDATE -> handleDataUpdate(message);
                case NOTIFICATION -> handleNotification(message);
                case ERROR -> handleError(message);
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
                @SuppressWarnings("unchecked")
                List<NavigationItem> updatedList = (List<NavigationItem>) message.getData();
                this.controllerManager.getNavigationService().updateNavigationUI(updatedList);
            }
            case "INBOX_UPDATED" -> {
                Integer count = (Integer) message.getData();
                this.controllerManager.getInboxService().updateInboxCount(count);
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
    }

    private void handleError(ThreadMessage message) {
        logger.error("🔴 Erreur reçue du thread de logique: {}", message.getContent());

        // Mettre à jour l'interface pour afficher l'erreur
        if (Platform.isFxApplicationThread()) {
            showErrorToUser(message.getContent());
        } else {
            Platform.runLater(() -> showErrorToUser(message.getContent()));
        }
    }

    private void showErrorToUser(String errorMessage) {
        logger.error("Affichage de l'erreur à l'utilisateur: {}", errorMessage);
    }

    public void initializeUI() {
        logger.info("🟢 Initialisation de l'interface utilisateur");
        controllerManager.initializeAllServices();
    }


}

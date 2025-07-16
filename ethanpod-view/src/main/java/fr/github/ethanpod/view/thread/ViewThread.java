package fr.github.ethanpod.view.thread;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.layout.NavigationContainer;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ViewThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(ViewThread.class);
    private static final String THREAD_NAME = "ViewThread";

    // Instance statique pour accès depuis Main JavaFX
    private static ViewThread instance;

    private final BlockingQueue<ThreadMessage> messageQueue;
    private final MessageRouter messageRouter;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AsyncServiceManager asyncServiceManager;
    private UIUpdateCallback uiUpdateCallback;

    public ViewThread() {
        this.messageRouter = MessageRouter.getInstance();
        this.messageQueue = this.messageRouter.registerThread(THREAD_NAME);
        this.asyncServiceManager = new AsyncServiceManager();
        instance = this;
    }

    public static ViewThread getInstance() {
        return instance;
    }

    @Override
    public void run() {
        logger.info("🟢 Thread View démarré - Interface utilisateur");

        messageRouter.sendRequestToLogic("UI_READY", null, MessageType.NOTIFICATION, null);

        while (running.get()) {
            try {
                processIncomingMessages();
                Thread.sleep(100); // Éviter une boucle trop intensive

                // Vérifier si le thread principal demande l'arrêt
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("🟢 Thread View interrompu volontairement");
                    break;
                }
            } catch (InterruptedException _) {
                logger.info("🟢 Thread View interrompu");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.error("Erreur dans le thread d'interface", e);
            }
        }

        logger.info("🟢 Thread View terminé");
    }

    private void processIncomingMessages() {
        ThreadMessage message = messageQueue.poll();

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
        // Déléguer au ServiceManager pour router vers le bon service
        asyncServiceManager.handleResponse(message);
    }

    private void handleDataUpdate(ThreadMessage message) {
        String content = message.getContent();

        switch (content) {
            case "DATA_REFRESHED" -> {
                @SuppressWarnings("unchecked")
                List<NavigationItem> updatedList = (List<NavigationItem>) message.getData();
                updateNavigationUI(updatedList);
            }
            case "INBOX_UPDATED" -> {
                Integer count = (Integer) message.getData();
                updateInboxCount(count);
            }
            default -> logger.warn("Contenu de mise à jour non géré: {}", content);
        }
    }

    private void handleNotification(ThreadMessage message) {
        logger.info("🟢 Notification reçue: {}", message.getContent());

        if ("LOGIC_READY".equals(message.getContent())) {
            asyncServiceManager.initializeAllServices();
            logger.info("🟢 Services initialisés, en attente de JavaFX");
            //initializeUI();
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

    // ================================
    // Fonctions d'Interface Asynchrones
    // ================================

    public void initializeUI() {
        logger.info("🟢 Initialisation de l'interface utilisateur");
        loadNavigationData();
        //loadInboxCount();
    }

    public void loadNavigationData() {
        logger.info("🟢 Chargement des données de navigation");

        asyncServiceManager.getNavigationService().getListAsync()
                .thenAccept(navigationList -> {
                    logger.info("🟢 {} éléments de navigation reçus", navigationList.size());
                    updateNavigationUI(navigationList);
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement de la navigation : {}", throwable.getMessage());
                    return null;
                });
    }

    public void loadInboxCount() {
        logger.info("🟢 Chargement du nombre d'éléments inbox");

        asyncServiceManager.getInboxService().getInboxCountAsync()
                .thenAccept(count -> {
                    logger.info("🟢 {} éléments dans l'inbox", count);
                    updateInboxCount(count);
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement du compte inbox", throwable);
                    return null;
                });
    }

    public void refreshAllData() {
        logger.info("🟢 Rafraîchissement de toutes les données");
        asyncServiceManager.refreshAllData();
    }

    /**
     * Marque un élément de l'inbox comme lu
     */
    public void markInboxItemAsRead(String itemId) {
        asyncServiceManager.getInboxService().markAsReadAsync()
                .thenAccept(success -> {
                    if (success) {
                        logger.info("🟢 Élément {} marqué comme lu", itemId);
                        // Recharger le count
                        loadInboxCount();
                    } else {
                        logger.warn("🟡 Échec du marquage comme lu pour {}", itemId);
                    }
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du marquage comme lu", throwable);
                    return null;
                });
    }

    private void updateNavigationUI(List<NavigationItem> navigationList) {
        Platform.runLater(() -> doUpdateNavigationUI(navigationList));
    }

    private void updateInboxCount(Integer count) {
        Platform.runLater(() -> doUpdateInboxCount(count));
    }

    private void doUpdateNavigationUI(List<NavigationItem> navigationList) {
        try {
            if (uiUpdateCallback != null) {
                logger.info("🟢 Interface mise à jour avec {} éléments", navigationList.size());
                this.uiUpdateCallback.updateNavigationList(navigationList);
            } else {
                logger.warn("NavigationContainer n'est pas encore initialisé");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'interface", e);
        }
    }

    private void doUpdateInboxCount(Integer count) {
        try {
            if (uiUpdateCallback != null) {
                logger.info("🟢 Compteur inbox mis à jour: {}", count);
                this.uiUpdateCallback.updateInboxCount(count);
            } else {
                logger.warn("NavigationContainer n'est pas encore initialisé");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du compteur inbox", e);
        }
    }

    public void setNavigationContainer(NavigationContainer navigationContainer) {
        this.uiUpdateCallback = navigationContainer;
        logger.info("🟢 NavigationContainer configuré dans {}", THREAD_NAME);
    }

    public void stop() {
        logger.info("🟢 Arrêt du thread d'interface demandé");
        running.set(false);
        asyncServiceManager.stopAllServices();
    }

    public void onJavaFXReady() {
        logger.info("🟢 JavaFX est prêt - Interface utilisateur disponible");
        initializeUI();
    }
}
package fr.github.ethanpod.view;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncNavigationService;
import fr.github.ethanpod.view.layout.NavigationContainer;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ViewThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(ViewThread.class);
    // Instance statique pour accès depuis Main JavaFX
    private static ViewThread instance;
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AsyncNavigationService navigationService;
    private NavigationContainer navigationContainer;

    public ViewThread(BlockingQueue<ThreadMessage> messageQueue) {
        this.messageQueue = messageQueue;
        this.navigationService = new AsyncNavigationService(messageQueue);
        instance = this;
    }

    public static ViewThread getInstance() {
        return instance;
    }

    @Override
    public void run() {
        logger.info("🟢 Thread View démarré - Interface utilisateur");

        // Notifier que l'interface est prête
        sendNotification("UI_READY");

        while (running.get()) {
            try {
                // Traiter les messages entrants du thread de logique
                processIncomingMessages();

                Thread.sleep(100); // Éviter une boucle trop intensive

                // Vérifier si le thread principal demande l'arrêt
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("🟢 Thread View interrompu volontermement");
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
        if (message != null && "ViewThread".equals(message.getReceiver())) {
            logger.info("🟢 View reçu: {}", message);

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
        // Déléguer au service pour gérer les réponses asynchrones
        navigationService.handleResponse(message);
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
            // La logique est prête, on peut commencer à faire des requêtes
            initializeUI();
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
        // Ici, vous pourriez afficher une notification d'erreur à l'utilisateur
        logger.error("Affichage de l'erreur à l'utilisateur: {}", errorMessage);
        // Exemple: Alert, Notification, etc.
    }

    // ================================
    // Fonctions d'Interface Asynchrones
    // ================================

    public void initializeUI() {
        logger.info("🟢 View: Initialisation de l'interface utilisateur");

        // Charger les données de navigation de manière asynchrone
        loadNavigationData();
        loadInboxCount();
    }

    public void loadNavigationData() {
        logger.info("🟢 View: Chargement des données de navigation");

        navigationService.getListAsync().thenAccept(navigationList -> {
            logger.info("🟢 View: {} éléments de navigation reçus", navigationList.size());
            updateNavigationUI(navigationList);
        }).exceptionally(throwable -> {
            logger.error("🔴 Erreur lors du chargement de la navigation", throwable);
            return null;
        });
    }

    public void loadInboxCount() {
        logger.info("🟢 View: Chargement du nombre d'éléments inbox");

        navigationService.getInboxCountAsync().thenAccept(count -> {
            logger.info("🟢 View: {} éléments dans l'inbox", count);
            updateInboxCount(count);
        }).exceptionally(throwable -> {
            logger.error("🔴 Erreur lors du chargement du compte inbox", throwable);
            return null;
        });
    }

    public void refreshAllData() {
        logger.info("🟢 View: Rafraîchissement de toutes les données");
        navigationService.refreshData();
    }

    private void updateNavigationUI(List<NavigationItem> navigationList) {
        // Mettre à jour l'interface sur le thread JavaFX
        if (Platform.isFxApplicationThread()) {
            doUpdateNavigationUI(navigationList);
        } else {
            Platform.runLater(() -> doUpdateNavigationUI(navigationList));
        }
    }

    private void doUpdateNavigationUI(List<NavigationItem> navigationList) {
        try {
            if (navigationContainer != null) {
                // Ici, vous mettriez à jour votre NavigationContainer
                logger.info("🟢 Interface mise à jour avec {} éléments", navigationList.size());
                //navigationContainer.updateItems(navigationList);
            } else {
                logger.warn("NavigationContainer n'est pas encore initialisé");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'interface", e);
        }
    }

    private void updateInboxCount(Integer count) {
        if (Platform.isFxApplicationThread()) {
            doUpdateInboxCount(count);
        } else {
            Platform.runLater(() -> doUpdateInboxCount(count));
        }
    }

    private void doUpdateInboxCount(Integer count) {
        try {
            // Mettre à jour le compteur d'inbox dans l'interface
            logger.info("🟢 Compteur inbox mis à jour: {}", count);
            if (navigationContainer != null) {
                //navigationContainer.updateInboxCount(count);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du compteur inbox", e);
        }
    }

    public void setNavigationContainer(NavigationContainer navigationContainer) {
        this.navigationContainer = navigationContainer;
        logger.info("🟢 NavigationContainer configuré dans ViewThread");
    }

    private void sendNotification(String content) {
        try {
            ThreadMessage message = new ThreadMessage(content, "ViewThread", "LogicThread",
                    MessageType.NOTIFICATION, null, null);
            messageQueue.put(message);
            logger.debug("🟢 Notification envoyée: {}", content);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Erreur lors de l'envoi de la notification", e);
        }
    }

    public void stop() {
        logger.info("🟢 Arrêt du thread d'interface demandé");
        running.set(false);
    }

    // Méthodes pour l'intégration avec JavaFX
    public void onJavaFXReady() {
        logger.info("🟢 JavaFX est prêt - Interface utilisateur disponible");
        // Vous pouvez maintenant initialiser l'UI si ce n'est pas déjà fait
        if (navigationContainer == null) {
            logger.info("🟢 Initialisation différée de l'interface utilisateur");
            // Tentative de récupération du NavigationContainer depuis MainLayout
            // Cette partie dépend de votre implémentation de MainLayout
        }
    }

    public AsyncNavigationService getNavigationService() {
        return navigationService;
    }
}
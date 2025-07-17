package fr.github.ethanpod.view.thread.controller;

import javafx.application.Platform;

public class InboxController extends Controller {

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


    public void updateInboxCount(Integer count) {
        Platform.runLater(() -> doUpdateInboxCount(count));
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

    @Override
    void initializeUI() {
        loadInboxCount();
    }
}

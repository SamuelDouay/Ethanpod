package fr.github.ethanpod.view.thread.controller;

import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.thread.callback.InboxCountUpdatedEvent;
import javafx.application.Platform;

public class InboxController extends Controller {

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
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


    public void updateInboxCount(Integer count) {
        Platform.runLater(() -> {
            InboxCountUpdatedEvent event = new InboxCountUpdatedEvent(
                    "InboxController", count
            );
            eventManager.publishEvent(event);
        });
    }

    @Override
    void initializeUI() {
        loadInboxCount();
    }
}

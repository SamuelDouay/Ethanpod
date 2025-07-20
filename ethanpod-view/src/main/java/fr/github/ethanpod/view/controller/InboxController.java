package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.controller.ui.InboxUIController;

public class InboxController extends Controller {
    private final InboxUIController uiController;

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
        this.uiController = new InboxUIController();
    }

    public void loadInboxCount() {
        logger.info("🟢 Chargement du nombre d'éléments inbox");

        asyncServiceManager.getInboxService().getInboxCountAsync()
                .thenAccept(count -> {
                    logger.info("🟢 {} éléments dans l'inbox", count);
                    uiController.updateInboxCount(count);
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement du compte inbox", throwable);
                    return null;
                });
    }

    @Override
    void initializeUI() {
        loadInboxCount();
    }
}

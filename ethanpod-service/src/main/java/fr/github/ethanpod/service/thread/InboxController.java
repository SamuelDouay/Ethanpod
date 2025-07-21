package fr.github.ethanpod.service.thread;

import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class InboxController extends Controller {

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadInboxCount() {
        logger.info("🟢 Chargement du nombre d'éléments inbox");

        asyncServiceManager.getInboxService().getInboxCountAsync()
                .thenAccept(count -> {
                    logger.info("🟢 {} éléments dans l'inbox", count);
                    messageRouter.sendRequestToUiEventFromView("INBOX_COUNT", null, MessageType.EVENT, count);
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

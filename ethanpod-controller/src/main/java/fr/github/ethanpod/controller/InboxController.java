package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class InboxController extends Controller {

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadInboxCount() {
        logger.info("🟢 Chargement du nombre d'éléments inbox");

        asyncServiceManager.getInboxService().getInboxCountAsync()
                .thenAccept(result -> {
                    logger.info("🟢 {} éléments dans l'inbox", result.data());
                    messageRouter.sendRequestToUiEventFromView("INBOX_COUNT_UPDATED", result.requestId(), MessageType.EVENT, result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement du compte inbox", throwable);
                    return null;
                });
    }

    public void loadInboxTop8() {
        logger.info("🟢 Chargement du top 8 Inbox");

        asyncServiceManager.getInboxService().getTop8InInbox()
                .thenAccept(result -> {
                    logger.info("🟢 {} éléments dans l'inbox", result.data().size());
                    messageRouter.sendRequestToUiEventFromView("GET_TOP8_INBOX_UPDATE", result.requestId(), MessageType.EVENT, result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement du top 8 inbox", throwable);
                    return null;
                });
    }


    @Override
    void initializeUI() {
        loadInboxCount();
        loadInboxTop8();
    }
}

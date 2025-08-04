package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class InboxController extends Controller {

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }


    public void loadInboxCount() {
        long startTime = System.currentTimeMillis();
        logger.info("Chargement du nombre d'éléments inbox");

        asyncServiceManager.getInboxService().getInboxCountAsync()
                .thenAccept(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logRequestTime(result.requestId(), executionTime);
                    messageRouter.sendEvent(EventType.INBOX_COUNT_UPDATED, result.requestId(), result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("Erreur lors du chargement du compte inbox", throwable);
                    return null;
                });
    }

    public void loadInboxTop8() {
        long startTime = System.currentTimeMillis();
        logger.info("Chargement du top 8 Inbox");
        asyncServiceManager.getInboxService().getTop8InInbox()
                .thenAccept(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logRequestTime(result.requestId(), executionTime);
                    messageRouter.sendEvent(EventType.INBOX_TOP8_UPDATED, result.requestId(), result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement du top 8 inbox", throwable);
                    return null;
                });
    }


    @Override
    void initializeUI() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(this::loadInboxTop8),
                CompletableFuture.runAsync(this::loadInboxCount)
        );
    }
}

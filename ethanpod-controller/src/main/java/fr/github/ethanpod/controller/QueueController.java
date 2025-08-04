package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class QueueController extends Controller {
    public QueueController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadQueueTop8() {
        long startTime = System.currentTimeMillis();
        logger.info("Chargement du top 8 Queue");

        asyncServiceManager.getQueueService().getQueueTop8()
                .thenAccept(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logRequestTime(result.requestId(), executionTime);
                    messageRouter.sendEvent(EventType.QUEUE_TOP8_UPDATED, result.requestId(), result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("Erreur lors du chargement du top 8 queue", throwable);
                    return null;
                });

    }

    @Override
    void initializeUI() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(this::loadQueueTop8)
        );
    }
}

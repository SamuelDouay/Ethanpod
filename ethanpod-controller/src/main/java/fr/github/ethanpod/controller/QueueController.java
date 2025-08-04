package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class QueueController extends Controller {
    public QueueController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadQueueTop8() {
        executeAsyncOperation(
                "Chargement du top 8 Queue",
                () -> asyncServiceManager.getQueueService().getQueueTop8(),
                EventType.QUEUE_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 Queue"
        );
    }

    @Override
    void initializeUI() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(this::loadQueueTop8)
        );
    }
}

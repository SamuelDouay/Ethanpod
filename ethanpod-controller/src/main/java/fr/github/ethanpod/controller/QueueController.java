package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class QueueController extends Controller {
    public QueueController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadQueueTop8() {
        logger.info("🟢 Chargement du top 8 Queue");

        asyncServiceManager.getQueueService().getQueueTop8()
                .thenAccept(result -> {
                    logger.info("🟢 {} éléments dans la queue", result.data().size());
                    messageRouter.sendRequestToUiEventFromView("GET_TOP8_QUEUE_UPDATE", result.requestId(), MessageType.EVENT, result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement du top 8 queue", throwable);
                    return null;
                });

    }

    @Override
    void initializeUI() {
        loadQueueTop8();
    }
}

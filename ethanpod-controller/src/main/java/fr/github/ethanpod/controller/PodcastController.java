package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class PodcastController extends Controller {

    public PodcastController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadTop8PodcastRead() {
        long startTime = System.currentTimeMillis();
        logger.info("Chargement du top 8 Podcast");

        asyncServiceManager.getPodcastService().getTop8PodcastRead()
                .thenAccept(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logRequestTime(result.requestId(), executionTime);
                    messageRouter.sendRequestToUiEventFromView("GET_TOP8_PODCAST_UPDATED", result.requestId(), MessageType.EVENT, result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("Erreur lors du chargement de la navigation : {}", throwable.getMessage());
                    return null;
                });
    }

    @Override
    void initializeUI() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(this::loadTop8PodcastRead)
        );
    }
}

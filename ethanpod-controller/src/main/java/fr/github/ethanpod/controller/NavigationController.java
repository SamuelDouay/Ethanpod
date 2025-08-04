package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class NavigationController extends Controller {

    public NavigationController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadNavigationData() {
        long startTime = System.currentTimeMillis();
        logger.info("Chargement des données de navigation");

        asyncServiceManager.getNavigationService().getListAsync()
                .thenAccept(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logRequestTime(result.requestId(), executionTime);
                    messageRouter.sendEvent(EventType.NAVIGATION_UPDATED, result.requestId(), result.data());
                })
                .exceptionally(throwable -> {
                    logger.error("Erreur lors du chargement de la navigation : {}", throwable.getMessage());
                    return null;
                });

    }

    @Override
    void initializeUI() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(this::loadNavigationData)
        );
    }
}

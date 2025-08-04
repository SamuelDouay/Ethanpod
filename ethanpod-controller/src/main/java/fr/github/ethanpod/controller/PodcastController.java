package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class PodcastController extends Controller {

    public PodcastController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadTop8PodcastRead() {
        executeAsyncOperation(
                "Chargement du top 8 Podcast",
                () -> asyncServiceManager.getPodcastService().getTop8PodcastRead(),
                EventType.PODCAST_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 podcast"
        );
    }

    @Override
    void initializeUI() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(this::loadTop8PodcastRead)
        );
    }
}

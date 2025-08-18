package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

public class DownloadController extends Controller {
    public DownloadController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadDownloadTop8() {
        executeAsyncOperation(
                "Chargement du top 8 Download",
                () -> asyncServiceManager.getDownloadService().getDownloadTop8(),
                EventType.DOWNLOAD_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 Download"
        );
    }

    @Override
    void initializeUI() {
        CompletableFuture.runAsync(this::loadDownloadTop8, UI_EXECUTOR).whenComplete((_, _) -> UI_EXECUTOR.shutdown());
    }
}

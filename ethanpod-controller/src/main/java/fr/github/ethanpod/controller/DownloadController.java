package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class DownloadController extends Controller {
    public DownloadController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadDownloadTop8(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du top 8 Download",
                () -> asyncServiceManager.getDownloadService().getDownloadTop8(message.id()),
                EventType.DOWNLOAD_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 Download"
        );
    }

    public void loadDownloadAll(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du all Download",
                () -> asyncServiceManager.getDownloadService().getDownloadAll(message.id(), (UserDataRequest) message.data()),
                EventType.EPISODE_ALL_UPDATED,
                "Erreur lors du chargement du all Download"
        );
    }
}

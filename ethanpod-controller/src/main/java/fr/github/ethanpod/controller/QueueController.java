package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class QueueController extends Controller {
    public QueueController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadQueueTop8(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du top 8 Queue",
                () -> asyncServiceManager.getQueueService().getQueueTop8(message.id()),
                EventType.QUEUE_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 Queue"
        );
    }

    public void loadQueueAll(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du all in Queue",
                () -> asyncServiceManager.getQueueService().getQueueAll(message.id(), (UserDataRequest) message.data()),
                EventType.EPISODE_ALL_UPDATED,
                "Erreur lors du chargement du all in Queue"
        );
    }
}

package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class PodcastController extends Controller {

    public PodcastController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadTop8PodcastRead(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du top 8 Podcast",
                () -> asyncServiceManager.getPodcastService().getTop8PodcastRead(message.id()),
                EventType.PODCAST_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 podcast"
        );
    }

    public void getPodcastById(ThreadMessage message) {
        executeAsyncOperation(
                "Find podcast n°" + message.data(),
                () -> asyncServiceManager.getPodcastService().getPodcastById(message.id(), (Integer) message.data()),
                EventType.PODCAST_BY_ID_UPDATED,
                "Podcast not found n°" + message.data()
        );
    }

    public void getSubscriptionAll(ThreadMessage message) {
        executeAsyncOperation(
                "getting all subscription",
                () -> asyncServiceManager.getPodcastService().getAllSubscription(message.id(), (UserDataRequest) message.data()),
                EventType.SUBSCRIPTION_ALL_UPDATED,
                "Error all subscription"
        );
    }
}

package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class EpisodeController extends Controller {
    public EpisodeController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void getEpisodeByPodcastId(ThreadMessage message) {
        executeAsyncOperation(
                "Getting episode by podcast n° " + message.data(),
                () -> asyncServiceManager.getEpisodeService().getEpisodeByPodcastId(message.id(), (UserDataRequest) message.data()),
                EventType.EPISODE_ALL_UPDATED,
                "Erreur episode by podcast n° " + message.data()
        );
    }
}

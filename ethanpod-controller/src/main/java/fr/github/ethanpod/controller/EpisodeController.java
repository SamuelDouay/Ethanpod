package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class EpisodeController extends Controller {
    public EpisodeController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void getEpisodeByPodcastId(Integer id) {
        executeAsyncOperation(
                "Getting episode by podcast n° " + id,
                () -> asyncServiceManager.getEpisodeService().getEpisodeByPodcastId(id),
                EventType.EPISODE_BY_PODCAST_ID_UPDATED,
                "Erreur episode by podcast n° " + id
        );
    }
}

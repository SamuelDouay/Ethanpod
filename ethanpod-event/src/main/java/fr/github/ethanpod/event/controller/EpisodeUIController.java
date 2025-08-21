package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.EpisodeByPodcastIdUpdatedEvent;

import java.util.List;

public class EpisodeUIController extends UIController {
    public EpisodeUIController() {
        // no param
    }

    public void updateEpisodeByPodcastId(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new EpisodeByPodcastIdUpdatedEvent(controllerName, episodeItems));
    }
}

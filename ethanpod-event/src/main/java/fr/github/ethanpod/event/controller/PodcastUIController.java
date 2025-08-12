package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.PodcastTop8UpdateEvent;

import java.util.List;

public class PodcastUIController extends UIController {

    public PodcastUIController() {
        // no param
    }

    public void updatePodcastTop8UI(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new PodcastTop8UpdateEvent(controllerName, episodeItems));
    }
}

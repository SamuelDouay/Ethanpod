package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.PodcastTop8UpdateEvent;
import javafx.application.Platform;

import java.util.List;

public class PodcastUIController extends UIController {

    public PodcastUIController() {
        // no param
    }

    public void updatePodcastTop8UI(List<EpisodeItem> episodeItems) {
        Platform.runLater(() -> {
            PodcastTop8UpdateEvent event = new PodcastTop8UpdateEvent(
                    "PodcastUIController", episodeItems
            );
            eventManager.publishEvent(event);
        });
    }
}

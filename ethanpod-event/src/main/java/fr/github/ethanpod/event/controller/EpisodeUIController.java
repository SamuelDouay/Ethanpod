package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.EpisodeAllUpdatedEvent;

import java.util.List;

public class EpisodeUIController extends UIController {
    public EpisodeUIController() {
        // no param
    }

    public void updateEpisodeAll(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new EpisodeAllUpdatedEvent(controllerName, episodeItems));
    }
}

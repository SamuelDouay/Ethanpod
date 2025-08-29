package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.HistoryAllUpdatedEvent;

import java.util.List;

public class HistoryUIController extends UIController {
    public HistoryUIController() {
        // no content
    }

    public void updateHistoryAllUI(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new HistoryAllUpdatedEvent(controllerName, episodeItems));
    }
}

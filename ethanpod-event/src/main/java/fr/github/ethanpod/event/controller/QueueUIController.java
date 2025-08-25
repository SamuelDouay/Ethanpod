package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.QueueAllUpdatedEvent;
import fr.github.ethanpod.event.QueueTop8UpdateEvent;

import java.util.List;

public class QueueUIController extends UIController {

    public QueueUIController() {
        // no param
    }

    public void updateQueueTop8UI(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new QueueTop8UpdateEvent(controllerName, episodeItems));
    }

    public void updateQueueAllUI(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new QueueAllUpdatedEvent(controllerName, episodeItems));
    }
}

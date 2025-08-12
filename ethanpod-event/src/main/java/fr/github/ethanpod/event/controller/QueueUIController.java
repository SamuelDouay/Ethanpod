package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.QueueTop8UpdateEvent;
import javafx.application.Platform;

import java.util.List;

public class QueueUIController extends UIController {

    public QueueUIController() {
        // no param
    }

    public void updateQueueTop8UI(List<EpisodeItem> episodeItems) {
        Platform.runLater(() -> {
            QueueTop8UpdateEvent event = new QueueTop8UpdateEvent(
                    "QueueUIController", episodeItems
            );
            eventManager.publishEvent(event);
        });
    }
}

package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.InboxCountUpdatedEvent;
import fr.github.ethanpod.event.InboxTop8UpdatedEvent;
import javafx.application.Platform;

import java.util.List;

public class InboxUIController extends UIController {

    public InboxUIController() {
        // no param
    }

    public void updateInboxCount(Integer count) {
        Platform.runLater(() -> {
            InboxCountUpdatedEvent event = new InboxCountUpdatedEvent(
                    "InboxController", count
            );
            eventManager.publishEvent(event);
        });
    }

    public void updateInboxTop8(List<EpisodeItem> episodeItems) {
        Platform.runLater(() -> {
            InboxTop8UpdatedEvent event = new InboxTop8UpdatedEvent(
                    "InboxController", episodeItems
            );
            eventManager.publishEvent(event);
        });
    }
}
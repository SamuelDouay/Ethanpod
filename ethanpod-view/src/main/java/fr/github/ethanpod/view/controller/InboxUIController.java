package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.view.event.InboxCountUpdatedEvent;
import javafx.application.Platform;

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
}
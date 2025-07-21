package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.view.event.InboxCountUpdatedEvent;
import fr.github.ethanpod.view.event.UIEventManager;
import javafx.application.Platform;

public class InboxUIController implements UIController {

    private final UIEventManager eventManager = UIEventManager.getInstance();

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
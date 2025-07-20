package fr.github.ethanpod.view.controller.ui;

import fr.github.ethanpod.view.controller.event.InboxCountUpdatedEvent;
import fr.github.ethanpod.view.controller.event.UIEventManager;
import javafx.application.Platform;

public class InboxUIController {

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
package fr.github.ethanpod.view.controller.ui;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.view.controller.event.NavigationUpdatedEvent;
import fr.github.ethanpod.view.controller.event.UIEventManager;
import javafx.application.Platform;

import java.util.List;

public class NavigationUIController {

    private final UIEventManager eventManager = UIEventManager.getInstance();

    public NavigationUIController() {
        // no param
    }

    public void updateNavigationUI(List<NavigationItem> navigationList) {
        Platform.runLater(() -> {
            NavigationUpdatedEvent event = new NavigationUpdatedEvent(
                    "NavigationController", navigationList
            );
            eventManager.publishEvent(event);
        });
    }
}
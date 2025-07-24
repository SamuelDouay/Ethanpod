package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.view.event.NavigationUpdatedEvent;
import javafx.application.Platform;

import java.util.List;

public class NavigationUIController extends UIController {

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
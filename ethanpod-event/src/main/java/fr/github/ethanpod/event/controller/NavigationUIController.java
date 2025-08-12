package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.event.NavigationUpdatedEvent;

import java.util.List;

public class NavigationUIController extends UIController {

    public NavigationUIController() {
        // no param
    }

    public void updateNavigationUI(List<NavigationItem> navigationList) {
        if (!isValidList(navigationList)) return;
        
        publishEvent(() -> new NavigationUpdatedEvent(controllerName, navigationList));
    }
}
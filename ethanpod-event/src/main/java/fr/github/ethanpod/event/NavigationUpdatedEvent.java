package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class NavigationUpdatedEvent extends UIEvent {
    public NavigationUpdatedEvent(String source, List<NavigationItem> navigationItems) {
        super(source, EventType.NAVIGATION_UPDATED);
        this.items = List.copyOf(navigationItems); // Copie défensive
    }
}

package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class NavigationUpdatedEvent extends UIEvent {
    private final List<NavigationItem> navigationItems;

    public NavigationUpdatedEvent(String source, List<NavigationItem> navigationItems) {
        super(source, EventType.NAVIGATION_UPDATED);
        this.navigationItems = List.copyOf(navigationItems); // Copie défensive
    }

    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    public int getItemCount() {
        return navigationItems.size();
    }
}

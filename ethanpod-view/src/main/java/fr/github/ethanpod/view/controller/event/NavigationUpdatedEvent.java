package fr.github.ethanpod.view.controller.event;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public class NavigationUpdatedEvent extends UIEvent {
    public static final String EVENT_TYPE = "NAVIGATION_UPDATED";

    private final List<NavigationItem> navigationItems;

    public NavigationUpdatedEvent(String source, List<NavigationItem> navigationItems) {
        super(source);
        this.navigationItems = List.copyOf(navigationItems); // Copie défensive
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    public int getItemCount() {
        return navigationItems.size();
    }
}

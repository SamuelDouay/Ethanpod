package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class SubscriptionAllUpdatedEvent extends UIEvent {
    public SubscriptionAllUpdatedEvent(String source, List<NavigationItem> navigationItems) {
        super(source, EventType.SUBSCRIPTION_ALL_UPDATED);
        this.navigationItems = List.copyOf(navigationItems); // Copie défensive
    }
}

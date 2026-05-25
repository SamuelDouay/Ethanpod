package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.Item;
import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public class NavigationAllUpdated {
    private final List<Item> items;

    public NavigationAllUpdated(List<NavigationItem> items) {
        this.items = List.copyOf(items); // copie défensive
    }

    public List<Item> getItems() {
        return items;
    }
}

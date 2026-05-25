package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.Item;

import java.util.List;

public abstract class AbstractEpisodeListUpdated<T extends Item> {
    private final List<T> items;

    public AbstractEpisodeListUpdated(List<T> items) {
        this.items = List.copyOf(items);
    }

    public List<T> getItems() {
        return items;
    }
}
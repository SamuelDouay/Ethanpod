package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.Item;

import java.util.List;

public class QueueAllUpdated {
    private final List<Item> items;

    public QueueAllUpdated(List<EpisodeItem> items) {
        this.items = List.copyOf(items); // copie défensive
    }

    public List<Item> getItems() {
        return items;
    }
}

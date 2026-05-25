package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.Item;

import java.util.List;

public class PodcastReadTop8Updated {
    private final List<Item> items;

    public PodcastReadTop8Updated(List<EpisodeItem> items) {
        this.items = List.copyOf(items); // copie défensive
    }

    public List<Item> getItems() {
        return items;
    }
}

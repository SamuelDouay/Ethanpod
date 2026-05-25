package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.Item;
import fr.github.ethanpod.core.item.PodcastItem;

public class PodcastByIdUpdated {
    private final Item item;

    public PodcastByIdUpdated(PodcastItem item) {
        this.item = item; // copie défensive
    }

    public Item getItem() {
        return item;
    }
}

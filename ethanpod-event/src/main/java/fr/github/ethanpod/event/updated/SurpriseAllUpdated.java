package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.Item;
import fr.github.ethanpod.core.item.SurpriseItem;

import java.util.List;

public class SurpriseAllUpdated {
    private final List<Item> items;

    public SurpriseAllUpdated(List<SurpriseItem> items) {
        this.items = List.copyOf(items); // copie défensive
    }

    public List<Item> getItems() {
        return items;
    }
}

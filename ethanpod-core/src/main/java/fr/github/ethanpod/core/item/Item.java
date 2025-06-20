package fr.github.ethanpod.core.item;

import java.util.UUID;

public class Item {
    private final UUID uuid;
    private boolean selected;

    public Item() {
        this.selected = false;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

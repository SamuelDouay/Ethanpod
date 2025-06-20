package fr.github.ethanpod.core.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {
    private final List<Item> itemList;

    public ItemManager() {
        this.itemList = new ArrayList<>();
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void setItemState(boolean state, UUID uuid) {
        for (Item item : itemList) {
            if (item.getUuid().equals(uuid)) {
                item.setSelected(state);
            } else {
                item.setSelected(false);
            }
        }
    }
}

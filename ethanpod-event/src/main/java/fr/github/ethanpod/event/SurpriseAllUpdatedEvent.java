package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class SurpriseAllUpdatedEvent extends UIEvent {
    public SurpriseAllUpdatedEvent(String source, List<SurpriseItem> surpriseItems) {
        super(source, EventType.SURPRISE_ALL_UPDATED);
        this.items = List.copyOf(surpriseItems); // Copie défensive
    }
}

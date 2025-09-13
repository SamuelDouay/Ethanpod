package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class InboxTop8UpdatedEvent extends UIEvent {
    public InboxTop8UpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.INBOX_TOP8_UPDATED);
        this.items = List.copyOf(episodeItems);
    }
}

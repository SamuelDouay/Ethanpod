package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class InboxAllUpdatedEvent extends UIEvent {
    private final List<EpisodeItem> episodeItems;

    public InboxAllUpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.INBOX_ALL_UPDATED);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }

}
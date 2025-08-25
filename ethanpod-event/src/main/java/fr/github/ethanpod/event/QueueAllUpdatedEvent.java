package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class QueueAllUpdatedEvent extends UIEvent {
    private final List<EpisodeItem> episodeItems;

    public QueueAllUpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.QUEUE_ALL_UPDATED);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }

}
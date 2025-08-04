package fr.github.ethanpod.view.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class QueueTop8UpdateEvent extends UIEvent {
    private final List<EpisodeItem> episodeItems;

    public QueueTop8UpdateEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.QUEUE_TOP8_UPDATED);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }

}

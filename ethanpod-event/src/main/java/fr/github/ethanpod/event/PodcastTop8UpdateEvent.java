package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class PodcastTop8UpdateEvent extends UIEvent {
    public PodcastTop8UpdateEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.PODCAST_TOP8_UPDATED);
        this.items = List.copyOf(episodeItems); // Copie défensive
    }
}

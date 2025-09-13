package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class EpisodeAllUpdatedEvent extends UIEvent {
    public EpisodeAllUpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.EPISODE_ALL_UPDATED);
        this.items = List.copyOf(episodeItems); // Copie défensive
    }

}
package fr.github.ethanpod.view.event;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class QueueTop8UpdateEvent extends UIEvent {
    public static final String EVENT_TYPE = "GET_TOP8_QUEUE_UPDATE";

    private final List<EpisodeItem> episodeItems;

    public QueueTop8UpdateEvent(String source, List<EpisodeItem> episodeItems) {
        super(source);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }

}

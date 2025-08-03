package fr.github.ethanpod.view.event;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class PodcastTop8UpdateEvent extends UIEvent {
    public static final String EVENT_TYPE = "GET_TOP8_PODCAST_UPDATED";

    private final List<EpisodeItem> episodeItems;

    public PodcastTop8UpdateEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EVENT_TYPE);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }
}

package fr.github.ethanpod.view.event;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class InboxTop8UpdatedEvent extends UIEvent {
    public static final String EVENT_TYPE = "GET_TOP8_INBOX_UPDATE";
    private final List<EpisodeItem> episodeItems;

    public InboxTop8UpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EVENT_TYPE);
        this.episodeItems = List.copyOf(episodeItems);
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }
}

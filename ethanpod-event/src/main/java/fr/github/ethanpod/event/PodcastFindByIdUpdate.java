package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.core.thread.EventType;

public class PodcastFindByIdUpdate extends UIEvent {
    private final PodcastItem podcastItem;

    public PodcastFindByIdUpdate(String source, PodcastItem podcastItem) {
        super(source, EventType.PODCAST_BY_ID_UPDATED);
        this.podcastItem = podcastItem;
    }

    public PodcastItem getPodcastItem() {
        return podcastItem;
    }
}

package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.*;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public abstract class UIEvent {
    public final EventType eventType;
    private final String source;
    protected PodcastItem podcastItem;
    protected List<EpisodeItem> episodeItems;
    protected List<NavigationItem> navigationItems;
    protected List<SurpriseItem> surpriseItems;
    protected List<Item> items;
    protected Integer count;

    protected UIEvent(String source, EventType eventType) {
        this.source = source;
        this.eventType = eventType;
    }

    public String getSource() {
        return source;
    }

    public EventType getEventType() {
        return eventType;
    }

    public List<Item> getItems() {
        return items;
    }

    public PodcastItem getPodcastItem() {
        return podcastItem;
    }

    public Integer getCount() {
        return count;
    }

    public List<SurpriseItem> getSurpriseItems() {
        return surpriseItems;
    }
}

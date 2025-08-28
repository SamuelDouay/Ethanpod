package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public abstract class UIEvent {
    public final EventType eventType;
    private final String source;
    protected PodcastItem podcastItem;
    protected List<EpisodeItem> episodeItems;
    protected List<NavigationItem> navigationItems;
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

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }

    public PodcastItem getPodcastItem() {
        return podcastItem;
    }

    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    public Integer getCount() {
        return count;
    }

}

package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class EpisodeByPodcastIdUpdatedEvent extends UIEvent {
    private final List<EpisodeItem> episodeItems;

    public EpisodeByPodcastIdUpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.EPISODE_BY_PODCAST_ID_UPDATED);

        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

    public List<EpisodeItem> getEpisodeItems() {
        return episodeItems;
    }
}

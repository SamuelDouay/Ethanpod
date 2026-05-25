package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class EpisodeByPodcastIdUpdated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public EpisodeByPodcastIdUpdated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

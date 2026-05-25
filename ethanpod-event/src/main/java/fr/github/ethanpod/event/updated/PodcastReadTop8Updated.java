package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class PodcastReadTop8Updated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public PodcastReadTop8Updated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

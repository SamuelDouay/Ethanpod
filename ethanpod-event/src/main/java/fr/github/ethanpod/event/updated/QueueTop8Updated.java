package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class QueueTop8Updated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public QueueTop8Updated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }

}

package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class QueueAllUpdated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public QueueAllUpdated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

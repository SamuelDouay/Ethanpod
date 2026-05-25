package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class InboxTop8Updated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public InboxTop8Updated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

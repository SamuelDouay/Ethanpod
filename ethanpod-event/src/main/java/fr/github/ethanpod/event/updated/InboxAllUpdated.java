package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class InboxAllUpdated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public InboxAllUpdated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class EpisodeAllUpdated extends AbstractEpisodeListUpdated<EpisodeItem> {
    public EpisodeAllUpdated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

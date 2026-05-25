package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class HistoryAllUpdated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public HistoryAllUpdated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

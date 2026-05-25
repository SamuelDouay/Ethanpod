package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class DownloadAllUpdated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public DownloadAllUpdated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }
}

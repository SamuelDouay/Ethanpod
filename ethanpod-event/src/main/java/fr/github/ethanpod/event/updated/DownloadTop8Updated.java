package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;

public class DownloadTop8Updated extends AbstractEpisodeListUpdated<EpisodeItem> {

    public DownloadTop8Updated(List<EpisodeItem> items) {
        super(items); // copie défensive
    }

}

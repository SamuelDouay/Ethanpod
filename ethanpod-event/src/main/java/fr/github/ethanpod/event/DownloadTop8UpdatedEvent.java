package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class DownloadTop8UpdatedEvent extends UIEvent {
    public DownloadTop8UpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.DOWNLOAD_TOP8_UPDATED);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

}
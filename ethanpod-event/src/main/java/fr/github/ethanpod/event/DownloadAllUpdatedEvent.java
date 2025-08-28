package fr.github.ethanpod.event;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;

import java.util.List;

public class DownloadAllUpdatedEvent extends UIEvent {
    public DownloadAllUpdatedEvent(String source, List<EpisodeItem> episodeItems) {
        super(source, EventType.DOWNLOAD_ALL_UPDATED);
        this.episodeItems = List.copyOf(episodeItems); // Copie défensive
    }

}
package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.DownloadTop8UpdatedEvent;

import java.util.List;

public class DownloadUIController extends UIController {

    public DownloadUIController() {
        // no param
    }

    public void updateDownloadTop8UI(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new DownloadTop8UpdatedEvent(controllerName, episodeItems));
    }
}
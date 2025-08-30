package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.event.PodcastFindByIdUpdate;
import fr.github.ethanpod.event.PodcastTop8UpdateEvent;
import fr.github.ethanpod.event.SubscriptionAllUpdatedEvent;

import java.util.List;

public class PodcastUIController extends UIController {

    public PodcastUIController() {
        // no param
    }

    public void updatePodcastTop8UI(List<EpisodeItem> episodeItems) {
        if (!isValidList(episodeItems)) return;

        publishEvent(() -> new PodcastTop8UpdateEvent(controllerName, episodeItems));
    }

    public void updatePodcastById(PodcastItem podcastItem) {
        if (!isValidData(podcastItem)) return;

        publishEvent(() -> new PodcastFindByIdUpdate(controllerName, podcastItem));
    }

    public void updateSubscriptionAll(List<NavigationItem> navigationItems) {
        if (!isValidList(navigationItems)) return;
        
        publishEvent(() -> new SubscriptionAllUpdatedEvent(controllerName, navigationItems));
    }
}

package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.InboxCountUpdatedEvent;
import fr.github.ethanpod.event.InboxTop8UpdatedEvent;

import java.util.List;

public class InboxUIController extends UIController {

    public InboxUIController() {
        // no param
    }

    public void updateInboxCount(Integer count) {
        if (!isValidData(count)) return;

        publishEvent(() -> new InboxCountUpdatedEvent(controllerName, count));
    }

    public void updateInboxTop8(List<EpisodeItem> episodes) {
        if (!isValidList(episodes)) return;

        publishEvent(() -> new InboxTop8UpdatedEvent(controllerName, episodes));
    }
}
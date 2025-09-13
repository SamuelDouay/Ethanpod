package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.event.SurpriseAllUpdatedEvent;

import java.util.List;

public class SurpriseUIController extends UIController {
    public SurpriseUIController() {
        // no param
    }

    public void updateSurpriseList(List<SurpriseItem> surpriseItems) {
        if (!isValidList(surpriseItems)) return;

        publishEvent(() -> new SurpriseAllUpdatedEvent(controllerName, surpriseItems));
    }
}

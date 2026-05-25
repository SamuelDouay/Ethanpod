package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.SurpriseItem;

import java.util.List;

public class SurpriseAllUpdated extends AbstractEpisodeListUpdated<SurpriseItem> {

    public SurpriseAllUpdated(List<SurpriseItem> items) {
        super(items); // copie défensive
    }
}

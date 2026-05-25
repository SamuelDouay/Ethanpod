package fr.github.ethanpod.event.updated;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public class NavigationAllUpdated extends AbstractEpisodeListUpdated<NavigationItem> {

    public NavigationAllUpdated(List<NavigationItem> items) {
        super(items); // copie défensive
    }
}

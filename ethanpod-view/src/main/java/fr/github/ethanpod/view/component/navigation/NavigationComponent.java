package fr.github.ethanpod.view.component.navigation;

import fr.github.ethanpod.core.item.NavigationItem;
import javafx.scene.layout.HBox;

public class NavigationComponent {
    public NavigationComponent() {
        // no parameter
    }

    public HBox createNavigationCard(NavigationItem item) {
        NavigationBuilder builder = new NavigationBuilder()
                .withTitle(item.getTitle())
                .setSelected(item.isSelected())
                .withBadgeCount(item.getNumber());

        if (item.getImageUrl() != null) {
            builder.withImage(item.getImageUrl());
        } else if (item.getIcon() != null) {
            builder.withIcon(item.getIcon());
        }

        return builder.build();
    }
}

package fr.github.ethanpod.view.component.navigation;

import fr.github.ethanpod.core.item.NavigationItem;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class NavigationComponent {
    public NavigationComponent() {
        // no parameter
    }

    public HBox createNavigationCard(NavigationItem item) {
        NavigationBuilder builder = new NavigationBuilder()
                .withTitle(item.getTitle())
                .setSelected(item.isSelected())
                .withBadgeCount(item.getNumber());

        if (item.isIcon()) {
            builder.withIcon(new FontIcon(item.getName()));
        } else {
            builder.withImage(item.getName());
        }
        return builder.build();
    }
}

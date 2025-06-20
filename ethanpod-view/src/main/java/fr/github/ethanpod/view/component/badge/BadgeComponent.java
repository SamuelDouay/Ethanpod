package fr.github.ethanpod.view.component.badge;

import fr.github.ethanpod.view.util.BadgeType;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class BadgeComponent {
    public BadgeComponent() {
        // no parameters
    }

    public HBox createGreenBadge(String text) {
        return new BadgeBuilder().withText(text).withBadgeType(BadgeType.GREEN).build();
    }

    public HBox createGreenBadge(String text, FontIcon icon) {
        return new BadgeBuilder().withText(text).withIcon(icon).withBadgeType(BadgeType.GREEN).build();
    }

    public HBox createGreenBadge(FontIcon icon) {
        return new BadgeBuilder().withIcon(icon).setIconOnly(true).withBadgeType(BadgeType.GREEN).build();
    }

    public HBox createRedBadge(String text) {
        return new BadgeBuilder().withText(text).withBadgeType(BadgeType.RED).build();
    }

    public HBox createRedBadge(String text, FontIcon icon) {
        return new BadgeBuilder().withText(text).withIcon(icon).withBadgeType(BadgeType.RED).build();
    }

    public HBox createRedBadge(FontIcon icon) {
        return new BadgeBuilder().withIcon(icon).setIconOnly(true).withBadgeType(BadgeType.RED).build();
    }

    public HBox createBlueBadge(String text) {
        return new BadgeBuilder().withText(text).withBadgeType(BadgeType.BLUE).build();
    }

    public HBox createBlueBadge(String text, FontIcon icon) {
        return new BadgeBuilder().withText(text).withIcon(icon).withBadgeType(BadgeType.BLUE).build();
    }

    public HBox createBlueBadge(FontIcon icon) {
        return new BadgeBuilder().withIcon(icon).setIconOnly(true).withBadgeType(BadgeType.BLUE).build();
    }

    public HBox createPurpleBadge(String text) {
        return new BadgeBuilder().withText(text).withBadgeType(BadgeType.PURPLE).build();
    }

    public HBox createPurpleBadge(String text, FontIcon icon) {
        return new BadgeBuilder().withText(text).withIcon(icon).withBadgeType(BadgeType.PURPLE).build();
    }

    public HBox createPurpleBadge(FontIcon icon) {
        return new BadgeBuilder().withIcon(icon).setIconOnly(true).withBadgeType(BadgeType.PURPLE).build();
    }
}
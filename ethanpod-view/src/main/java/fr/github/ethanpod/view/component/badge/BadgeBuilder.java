package fr.github.ethanpod.view.component.badge;

import fr.github.ethanpod.view.util.BadgeType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public class BadgeBuilder {
    // Button properties
    private String text;
    private FontIcon icon;
    private BadgeType badgeType;
    private boolean iconOnly;

    BadgeBuilder() {
        // no param
    }

    public BadgeBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public BadgeBuilder withIcon(FontIcon icon) {
        this.icon = icon;
        return this;
    }

    public BadgeBuilder withBadgeType(BadgeType badgeType) {
        this.badgeType = badgeType;
        return this;
    }

    public BadgeBuilder setIconOnly(boolean iconOnly) {
        this.iconOnly = iconOnly;
        return this;
    }

    public HBox build() {
        if (badgeType == null) {
            throw new IllegalStateException("Badge type must be specified");
        }

        HBox box = new HBox();
        box.setAlignment(POS_CENTER);
        box.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        box.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

        if (iconOnly && icon != null) {
            icon.setIconColor(badgeType.getTextColor());
            icon.setIconSize(BADGE_ICON_SIZE);

            box.setBackground(new Background(
                    new BackgroundFill(badgeType.getBackgroundColor(), DEFAULT_CORNER_RADII, INSETS_EMPTY)
            ));

            box.getChildren().add(icon);
            box.setPadding(BADGE_ICON_PADDING);


        } else {
            Label label = new Label(text);

            // Appliquer les propriétés communes
            label.setPrefWidth(BADGE_DEFAULT_WIDTH);
            label.setPadding(BADGE_DEFAULT_PADDING);
            label.setAlignment(POS_CENTER);

            // Ajouter une icône si fournie
            if (icon != null) {
                icon.setIconColor(badgeType.getTextColor());
                icon.setIconSize(BADGE_ICON_SIZE);
                label.setGraphic(icon);
                label.setGraphicTextGap(BADGE_TEXT_GAP);
            }

            // Appliquer les couleurs spécifiques au type
            label.setBackground(new Background(
                    new BackgroundFill(badgeType.getBackgroundColor(), DEFAULT_CORNER_RADII, INSETS_EMPTY)
            ));
            label.textFillProperty().set(badgeType.getTextColor());
            box.getChildren().add(label);
        }

        return box;
    }
}

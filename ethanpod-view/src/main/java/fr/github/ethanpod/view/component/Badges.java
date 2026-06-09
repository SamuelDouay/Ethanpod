package fr.github.ethanpod.view.component;

import fr.github.ethanpod.view.util.BadgeType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public final class Badges {

    private Badges() {
    }

    public static HBox blue(FontIcon icon) {
        return iconBadge(icon, BadgeType.BLUE);
    }

    public static HBox purple(FontIcon icon) {
        return iconBadge(icon, BadgeType.PURPLE);
    }

    public static HBox green(FontIcon icon) {
        return iconBadge(icon, BadgeType.GREEN);
    }

    public static HBox red(FontIcon icon) {
        return iconBadge(icon, BadgeType.RED);
    }

    public static HBox blue(String text) {
        return textBadge(text, BadgeType.BLUE);
    }

    public static HBox purple(String text) {
        return textBadge(text, BadgeType.PURPLE);
    }

    public static HBox green(String text) {
        return textBadge(text, BadgeType.GREEN);
    }

    public static HBox red(String text) {
        return textBadge(text, BadgeType.RED);
    }

    public static HBox blue(String text, FontIcon icon) {
        return textIconBadge(text, icon, BadgeType.BLUE);
    }

    public static HBox purple(String text, FontIcon icon) {
        return textIconBadge(text, icon, BadgeType.PURPLE);
    }

    public static HBox green(String text, FontIcon icon) {
        return textIconBadge(text, icon, BadgeType.GREEN);
    }

    public static HBox red(String text, FontIcon icon) {
        return textIconBadge(text, icon, BadgeType.RED);
    }

    // --- Implémentations ---

    private static HBox iconBadge(FontIcon icon, BadgeType type) {
        icon.setIconColor(type.getTextColor());
        icon.setIconSize(BADGE_ICON_SIZE);

        HBox box = baseBox();
        box.setBackground(new Background(
                new BackgroundFill(type.getBackgroundColor(), DEFAULT_CORNER_RADII, INSETS_EMPTY)));
        box.setPadding(BADGE_ICON_PADDING);
        box.getChildren().add(icon);
        return box;
    }

    private static HBox textBadge(String text, BadgeType type) {
        Label label = buildLabel(text, type);
        HBox box = baseBox();
        box.getChildren().add(label);
        return box;
    }

    private static HBox textIconBadge(String text, FontIcon icon, BadgeType type) {
        icon.setIconColor(type.getTextColor());
        icon.setIconSize(BADGE_ICON_SIZE);
        Label label = buildLabel(text, type);
        label.setGraphic(icon);
        label.setGraphicTextGap(BADGE_TEXT_GAP);
        HBox box = baseBox();
        box.getChildren().add(label);
        return box;
    }

    private static Label buildLabel(String text, BadgeType type) {
        Label label = new Label(text);
        label.setPrefWidth(BADGE_DEFAULT_WIDTH);
        label.setPadding(BADGE_DEFAULT_PADDING);
        label.setAlignment(POS_CENTER);
        label.setTextFill(type.getTextColor());
        label.setBackground(new Background(
                new BackgroundFill(type.getBackgroundColor(), DEFAULT_CORNER_RADII, INSETS_EMPTY)));
        return label;
    }

    private static HBox baseBox() {
        HBox box = new HBox();
        box.setAlignment(POS_CENTER);
        box.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        box.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        return box;
    }
}
package fr.github.ethanpod.view.component;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public final class Buttons {
    // --- Backgrounds pré-calculés, reconstruits au changement de thème ---
    private static Background bgPrimary = buildBg(ColorThemeConstants.getMain500(), DEFAULT_CORNER_RADII);
    private static Background bgPrimaryHover = buildBg(ColorThemeConstants.getMain400(), DEFAULT_CORNER_RADII);
    private static Background bgPrimaryPressed = buildBg(ColorThemeConstants.getMain600(), DEFAULT_CORNER_RADII);

    private static Background bgPrimaryCircle = buildBg(ColorThemeConstants.getMain400(), BUTTON_CIRCLE_RADII);
    private static Background bgPrimaryCircleHover = buildBg(ColorThemeConstants.getMain500(), BUTTON_CIRCLE_RADII);
    private static Background bgPrimaryCirclePressed = buildBg(ColorThemeConstants.getMain600(), BUTTON_CIRCLE_RADII);

    private static Border borderSecondary = buildBorder(DEFAULT_CORNER_RADII);
    private static Border borderSecondaryCircle = buildBorder(BUTTON_CIRCLE_RADII);

    static {
        ColorThemeConstants.addThemeChangeListener(t -> rebuildCachedStyles());
    }

    private Buttons() {
    }

    // --- API publique ---

    public static Button primary(String text) {
        Button b = base();
        b.setText(text);
        b.setTextFill(ColorThemeConstants.getMain950());
        b.setBackground(bgPrimary);
        b.setPadding(BUTTON_DEFAULT_PADDING);
        b.setBorder(null);
        attachHover(b, bgPrimaryHover, bgPrimary, bgPrimaryPressed);
        return b;
    }

    public static Button primary(String text, FontIcon icon) {
        Button b = primary(text);
        configureIcon(icon);
        b.setGraphic(icon);
        return b;
    }

    public static Button primaryIcon(FontIcon icon) {
        Button b = base();
        configureIcon(icon);
        b.setGraphic(icon);
        b.setPrefSize(BUTTON_ICON_ONLY_SIZE, BUTTON_ICON_ONLY_SIZE);
        b.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        b.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        b.setBackground(bgPrimaryCircle);
        b.setPadding(BUTTON_ICON_ONLY_PADDING);
        b.setBorder(null);
        attachHover(b, bgPrimaryCircleHover, bgPrimaryCircle, bgPrimaryCirclePressed);
        return b;
    }

    public static Button secondary(String text) {
        Button b = base();
        b.setText(text);
        b.setTextFill(ColorThemeConstants.getGrey950());
        b.setBackground(null);
        b.setPadding(BUTTON_SECONDARY_PADDING);
        b.setBorder(borderSecondary);
        attachHoverNullNormal(b, buildBg(ColorThemeConstants.getMain050(), DEFAULT_CORNER_RADII),
                buildBg(ColorThemeConstants.getMain100(), DEFAULT_CORNER_RADII));
        return b;
    }

    public static Button secondaryIcon(FontIcon icon) {
        Button b = base();
        configureIcon(icon);
        b.setGraphic(icon);
        b.setPrefSize(BUTTON_ICON_ONLY_SIZE, BUTTON_ICON_ONLY_SIZE);
        b.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        b.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        b.setBackground(null);
        b.setPadding(BUTTON_ICON_ONLY_PADDING);
        b.setBorder(borderSecondaryCircle);
        attachHoverNullNormal(b, buildBg(ColorThemeConstants.getMain100(), BUTTON_CIRCLE_RADII),
                buildBg(ColorThemeConstants.getMain300(), BUTTON_CIRCLE_RADII));
        return b;
    }

    public static Button tertiary(String text) {
        Button b = base();
        b.setText(text);
        b.setTextFill(ColorThemeConstants.getGrey950());
        b.setBackground(null);
        b.setBorder(null);
        b.setPadding(BUTTON_DEFAULT_PADDING);
        attachHoverNullNormal(b, buildBg(ColorThemeConstants.getMain050(), DEFAULT_CORNER_RADII),
                buildBg(ColorThemeConstants.getMain100(), DEFAULT_CORNER_RADII));
        return b;
    }

    public static Button tertiaryIcon(FontIcon icon) {
        Button b = base();
        configureIcon(icon);
        b.setGraphic(icon);
        b.setPrefSize(BUTTON_ICON_ONLY_SIZE, BUTTON_ICON_ONLY_SIZE);
        b.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        b.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        b.setBackground(null);
        b.setBorder(null);
        b.setPadding(BUTTON_ICON_ONLY_PADDING);
        attachHoverNullNormal(b, buildBg(ColorThemeConstants.getMain100(), BUTTON_CIRCLE_RADII),
                buildBg(ColorThemeConstants.getMain300(), BUTTON_CIRCLE_RADII));
        return b;
    }

    // --- Helpers privés ---

    private static Button base() {
        Button b = new Button();
        b.setFocusTraversable(true);
        b.setAlignment(POS_CENTER);
        return b;
    }

    private static void configureIcon(FontIcon icon) {
        icon.setIconColor(ColorThemeConstants.getGrey950());
        icon.setIconSize(BUTTON_ICON_SIZE);
    }

    /**
     * Hover avec retour à un background non-null
     */
    private static void attachHover(Button b, Background hover, Background normal, Background pressed) {
        b.setOnMouseEntered(_ -> b.setBackground(hover));
        b.setOnMouseExited(_ -> b.setBackground(normal));
        b.setOnMousePressed(_ -> b.setBackground(pressed));
        b.setOnMouseReleased(_ -> b.setBackground(b.isHover() ? hover : normal));
    }

    /**
     * Hover avec retour à null (secondary/tertiary)
     */
    private static void attachHoverNullNormal(Button b, Background hover, Background pressed) {
        b.setOnMouseEntered(_ -> b.setBackground(hover));
        b.setOnMouseExited(_ -> b.setBackground(null));
        b.setOnMousePressed(_ -> b.setBackground(pressed));
        b.setOnMouseReleased(_ -> b.setBackground(b.isHover() ? hover : null));
    }

    private static Background buildBg(Color color, CornerRadii radii) {
        return new Background(new BackgroundFill(color, radii, INSETS_EMPTY));
    }

    private static Border buildBorder(CornerRadii radii) {
        return new Border(new BorderStroke(
                ColorThemeConstants.getGrey950(), BorderStrokeStyle.SOLID, radii, BUTTON_BORDER_WIDTH));
    }

    private static void rebuildCachedStyles() {
        bgPrimary = buildBg(ColorThemeConstants.getMain500(), DEFAULT_CORNER_RADII);
        bgPrimaryHover = buildBg(ColorThemeConstants.getMain400(), DEFAULT_CORNER_RADII);
        bgPrimaryPressed = buildBg(ColorThemeConstants.getMain600(), DEFAULT_CORNER_RADII);
        bgPrimaryCircle = buildBg(ColorThemeConstants.getMain400(), BUTTON_CIRCLE_RADII);
        bgPrimaryCircleHover = buildBg(ColorThemeConstants.getMain500(), BUTTON_CIRCLE_RADII);
        bgPrimaryCirclePressed = buildBg(ColorThemeConstants.getMain600(), BUTTON_CIRCLE_RADII);
        borderSecondary = buildBorder(DEFAULT_CORNER_RADII);
        borderSecondaryCircle = buildBorder(BUTTON_CIRCLE_RADII);
    }
}
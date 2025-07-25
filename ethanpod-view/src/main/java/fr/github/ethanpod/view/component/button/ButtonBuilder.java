package fr.github.ethanpod.view.component.button;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.TypeButton;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public class ButtonBuilder {    // Button properties
    private String text;
    private FontIcon icon;
    private TypeButton typeButton;
    private boolean iconOnly;

    ButtonBuilder() {
    }

    public ButtonBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public ButtonBuilder withIcon(FontIcon icon) {
        this.icon = icon;
        return this;
    }

    public ButtonBuilder withType(TypeButton typeButton) {
        this.typeButton = typeButton;
        return this;
    }

    public ButtonBuilder setIconOnly(boolean iconOnly) {
        this.iconOnly = iconOnly;
        return this;
    }

    public Button build() {
        if (typeButton == null) {
            throw new IllegalStateException("Button type must be specified");
        }

        Button button = new Button();
        button.setFocusTraversable(true);
        button.setAlignment(POS_CENTER);

        if (iconOnly && icon != null) {
            button.setGraphic(icon);
            button.setPrefSize(BUTTON_ICON_ONLY_SIZE, BUTTON_ICON_ONLY_SIZE);
            button.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
            button.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        } else {
            button.setText(text != null ? text : "");
            if (icon != null) {
                configureIcon();
                button.setGraphic(icon);
            }
        }

        applyButtonStyle(button);

        return button;
    }

    private void configureIcon() {
        if (icon != null) {
            icon.setIconColor(ColorThemeConstants.getGrey950());
            icon.setIconSize(BUTTON_ICON_SIZE);
        }
    }

    private void applyButtonStyle(Button button) {
        // Apply base styling based on button type
        switch (typeButton) {
            case PRIMARY:
                applyPrimaryStyle(button);
                break;
            case SECONDARY:
                applySecondaryStyle(button);
                break;
            case TERTIARY:
                applyTertiaryStyle(button);
                break;
        }
    }

    private void applyPrimaryStyle(Button button) {
        // Styling common for both regular and icon-only buttons
        configureIcon();

        // Button-specific styling
        if (iconOnly) {
            button.setBackground(createCircleBackground(ColorThemeConstants.getMain400()));
            button.setPadding(BUTTON_ICON_ONLY_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain500(),
                    ColorThemeConstants.getMain400(),
                    ColorThemeConstants.getMain600(),
                    true);
        } else {
            button.textFillProperty().set(ColorThemeConstants.getMain950());
            button.setBackground(createBackground(ColorThemeConstants.getMain500()));
            button.setPadding(BUTTON_DEFAULT_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain400(),
                    ColorThemeConstants.getMain500(),
                    ColorThemeConstants.getMain600(),
                    false);
        }

        // Common settings
        button.setBorder(null);
    }

    private void applySecondaryStyle(Button button) {
        // Styling common for both regular and icon-only buttons
        configureIcon();
        button.setBackground(null);

        // Button-specific styling
        if (iconOnly) {
            button.setBorder(createCircleBorder());
            button.setPadding(BUTTON_ICON_ONLY_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain100(),
                    null,
                    ColorThemeConstants.getMain300(),
                    true);
        } else {
            button.textFillProperty().set(ColorThemeConstants.getGrey950());
            button.setBorder(createBorder());
            button.setPadding(BUTTON_SECONDARY_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain050(),
                    null,
                    ColorThemeConstants.getMain100(),
                    false);
        }
    }

    private void applyTertiaryStyle(Button button) {
        // Styling common for both regular and icon-only buttons
        configureIcon();
        button.setBackground(null);
        button.setBorder(null);

        // Button-specific styling
        if (iconOnly) {
            button.setPadding(BUTTON_ICON_ONLY_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain100(),
                    null,
                    ColorThemeConstants.getMain300(),
                    true);
        } else {
            button.textFillProperty().set(ColorThemeConstants.getGrey950());
            button.setPadding(BUTTON_DEFAULT_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain050(),
                    null,
                    ColorThemeConstants.getMain100(),
                    false);
        }
    }

    private void setMouseHandlers(Button button, Color hoverColor, Color normalColor, Color pressedColor, boolean isCircle) {
        button.setOnMouseEntered(_ -> button.setBackground(changeColor(isCircle, hoverColor)));

        button.setOnMouseExited(_ -> button.setBackground(normalColor == null ? null : (changeColor(isCircle, normalColor))));

        button.setOnMousePressed(_ -> button.setBackground(changeColor(isCircle, pressedColor)));

        button.setOnMouseReleased(_ -> {
            if (button.isHover()) {
                button.setBackground(changeColor(isCircle, hoverColor));
            } else {
                button.setBackground(normalColor == null ? null : (changeColor(isCircle, normalColor)));
            }
        });
    }

    private Background changeColor(boolean isCircle, Color color) {
        return isCircle ? createCircleBackground(color) : createBackground(color);
    }

    private Background createBackground(Color color) {
        return new Background(new BackgroundFill(color, DEFAULT_CORNER_RADII, INSETS_EMPTY));
    }

    private Background createCircleBackground(Color color) {
        return new Background(new BackgroundFill(color, BUTTON_CIRCLE_RADII, INSETS_EMPTY));
    }

    private Border createBorder() {
        return new Border(new BorderStroke(
                ColorThemeConstants.getGrey950(),
                BorderStrokeStyle.SOLID,
                DEFAULT_CORNER_RADII,
                BUTTON_BORDER_WIDTH
        ));
    }

    private Border createCircleBorder() {
        return new Border(new BorderStroke(
                ColorThemeConstants.getGrey950(),
                BorderStrokeStyle.SOLID,
                BUTTON_CIRCLE_RADII,
                BUTTON_BORDER_WIDTH
        ));
    }
}
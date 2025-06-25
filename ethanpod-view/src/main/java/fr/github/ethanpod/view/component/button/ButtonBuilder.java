package fr.github.ethanpod.view.component.button;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.TypeButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public class ButtonBuilder {
    // Button properties
    private String text;
    private FontIcon icon;
    private TypeButton typeButton;
    private boolean iconOnly;

    // Customization properties
    private Color backgroundColor;
    private Color textColor;
    private Color iconColor;
    private Color borderColor;

    /**
     * Private constructor to enforce builder pattern usage
     */
    ButtonBuilder() {
    }

    /**
     * Set the button text
     *
     * @param text The text to display on the button
     * @return This builder instance for chaining
     */
    public ButtonBuilder withText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Set the button icon
     *
     * @param icon The FontIcon to display on the button
     * @return This builder instance for chaining
     */
    public ButtonBuilder withIcon(FontIcon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Set the button type
     *
     * @param typeButton The type of button (PRIMARY, SECONDARY, TERTIARY)
     * @return This builder instance for chaining
     */
    public ButtonBuilder withType(TypeButton typeButton) {
        this.typeButton = typeButton;
        return this;
    }

    /**
     * Set the button to display only an icon (circular button)
     *
     * @param iconOnly Whether the button should display only an icon
     * @return This builder instance for chaining
     */
    public ButtonBuilder setIconOnly(boolean iconOnly) {
        this.iconOnly = iconOnly;
        return this;
    }

    /**
     * Set a custom background color for the button
     *
     * @param backgroundColor The background color to use
     * @return This builder instance for chaining
     */
    public ButtonBuilder withBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set a custom text color for the button
     *
     * @param textColor The text color to use
     * @return This builder instance for chaining
     */
    public ButtonBuilder withTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Set a custom icon color for the button
     *
     * @param iconColor The icon color to use
     * @return This builder instance for chaining
     */
    public ButtonBuilder withIconColor(Color iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    /**
     * Set a custom border color for the button
     *
     * @param borderColor The border color to use
     * @return This builder instance for chaining
     */
    public ButtonBuilder withBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * Build and return the button
     *
     * @return The constructed button
     */
    public Button build() {
        if (typeButton == null) {
            throw new IllegalStateException("Button type must be specified");
        }

        Button button = new Button();
        button.setFocusTraversable(true);
        button.setAlignment(Pos.CENTER);

        if (iconOnly && icon != null) {
            // Icon-only button (circular)
            button.setGraphic(icon);
            button.setPrefSize(ICON_BUTTON_SIZE, ICON_BUTTON_SIZE);
            button.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            button.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        } else {
            // Text button (with optional icon)
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
            icon.setIconColor(iconColor != null ? iconColor : ColorThemeConstants.getGrey950());
            icon.setIconSize(ICON_SIZE);
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
            Color bgColor = backgroundColor != null ? backgroundColor : ColorThemeConstants.getMain400();
            button.setBackground(createCircleBackground(bgColor));
            button.setPadding(ICON_BUTTON_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain500(),
                    ColorThemeConstants.getMain400(),
                    ColorThemeConstants.getMain600(),
                    true);
        } else {
            button.textFillProperty().set(textColor != null ? textColor : ColorThemeConstants.getMain950());
            Color bgColor = backgroundColor != null ? backgroundColor : ColorThemeConstants.getMain500();
            button.setBackground(createBackground(bgColor));
            button.setPadding(PRIMARY_TERTIARY_PADDING);

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
            button.setPadding(ICON_BUTTON_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain100(),
                    null,
                    ColorThemeConstants.getMain300(),
                    true);
        } else {
            button.textFillProperty().set(textColor != null ? textColor : ColorThemeConstants.getGrey950());
            button.setBorder(createBorder());
            button.setPadding(SECONDARY_PADDING);

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
            button.setPadding(ICON_BUTTON_PADDING);

            // Set mouse event handlers
            setMouseHandlers(button,
                    ColorThemeConstants.getMain100(),
                    null,
                    ColorThemeConstants.getMain300(),
                    true);
        } else {
            button.textFillProperty().set(textColor != null ? textColor : ColorThemeConstants.getGrey950());
            button.setPadding(PRIMARY_TERTIARY_PADDING);

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
        return new Background(new BackgroundFill(color, CORNER_RADII, Insets.EMPTY));
    }

    private Background createCircleBackground(Color color) {
        return new Background(new BackgroundFill(color, CIRCLE_RADII, Insets.EMPTY));
    }

    private Border createBorder() {
        Color color = borderColor != null ? borderColor : ColorThemeConstants.getGrey950();
        return new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                CORNER_RADII,
                BORDER_WIDTH
        ));
    }

    private Border createCircleBorder() {
        Color color = borderColor != null ? borderColor : ColorThemeConstants.getGrey950();
        return new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                CIRCLE_RADII,
                BORDER_WIDTH
        ));
    }
}
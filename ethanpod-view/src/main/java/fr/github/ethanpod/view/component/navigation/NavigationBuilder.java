package fr.github.ethanpod.view.component.navigation;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;

public class NavigationBuilder {
    private static final double ICON_SIZE = 25.0;
    private static final double TITLE_MAX_WIDTH = 140.0;
    private static final double MAX_WIDTH = 224.0;
    private static final double SPACING = 14.0;
    private static final Insets PADDING = new Insets(6.0, 12.0, 6.0, 12.0);
    private static final String FONT = "Inter";

    private String title;
    private FontIcon icon;
    private String imageUrl;
    private int badgeCount;
    private boolean selected;

    public NavigationBuilder() {
        // no parameter
    }

    public NavigationBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public void withIcon(FontIcon icon) {
        this.icon = icon;
    }

    public void withImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public NavigationBuilder withBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
        return this;
    }

    public NavigationBuilder setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public HBox build() {
        Node graphic = createNodeGraphic();

        Label titleLabel = getTitleLabel();

        // Create icon and title container
        HBox iconTitleBox = new HBox(SPACING, graphic, titleLabel);
        iconTitleBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        iconTitleBox.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        iconTitleBox.setAlignment(Pos.CENTER);

        // Create spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create main container
        HBox mainBox = new HBox();
        mainBox.setPadding(PADDING);
        mainBox.setMaxWidth(MAX_WIDTH);
        mainBox.setAlignment(Pos.CENTER);

        // Add badge if needed
        if (badgeCount > 0) {
            Label badgeLabel = createBadgeLabel(badgeCount);
            mainBox.getChildren().addAll(iconTitleBox, spacer, badgeLabel);
        } else {
            mainBox.getChildren().addAll(iconTitleBox, spacer);
        }

        if (selected) {
            mainBox.setBackground(new Background(new BackgroundFill(
                    ColorThemeConstants.getMain100(), new CornerRadii(2.0), null)));
        } else {
            mainBox.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, null, null)));
        }

        return mainBox;
    }

    private Label getTitleLabel() {
        // Create title label
        Label titleLabel = new Label(title);
        titleLabel.setMaxWidth(TITLE_MAX_WIDTH);

        // Apply text styling
        if (selected) {
            titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 12));
            titleLabel.setTextFill(ColorThemeConstants.getMain950());
        } else {
            titleLabel.setFont(Font.font(FONT, FontPosture.REGULAR, 12));
            titleLabel.setTextFill(ColorThemeConstants.getGrey800());
        }
        return titleLabel;
    }

    private Node createNodeGraphic() {
        Node graphic;
        if (imageUrl != null) {
            // Create image view
            Image image = ImageCache.getImage(imageUrl);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(ICON_SIZE);
            imageView.setFitHeight(ICON_SIZE);
            graphic = imageView;
        } else if (icon != null) {
            // Configure icon
            icon.setIconSize((int) ICON_SIZE);

            if (!selected) {
                icon.setIconColor(ColorThemeConstants.getGrey800());
            } else {
                icon.setIconColor(ColorThemeConstants.getMain950());
            }

            graphic = icon;
        } else {
            // Create empty region as fallback
            Region emptyRegion = new Region();
            emptyRegion.setMinSize(ICON_SIZE, ICON_SIZE);
            emptyRegion.setPrefSize(ICON_SIZE, ICON_SIZE);
            graphic = emptyRegion;
        }
        return graphic;
    }

    private Label createBadgeLabel(int count) {
        Label numberLabel = new Label(String.valueOf(count));
        numberLabel.setTextFill(ColorThemeConstants.getMain950());
        numberLabel.setFont(Font.font(FONT, FontWeight.BOLD, 10));
        return numberLabel;
    }
}
package fr.github.ethanpod.view.component.navigation;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.FontThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public class NavigationBuilder {
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
        HBox iconTitleBox = new HBox(NAVIGATION_SPACING, graphic, titleLabel);
        iconTitleBox.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        iconTitleBox.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        iconTitleBox.setAlignment(POS_CENTER);

        // Create spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, PRIORITY_ALWAYS);

        // Create main container
        HBox mainBox = new HBox();
        mainBox.setPadding(NAVIGATION_PADDING);
        mainBox.setMaxWidth(NAVIGATION_MAX_WIDTH);
        mainBox.setAlignment(POS_CENTER);

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
        titleLabel.setMaxWidth(NAVIGATION_TITLE_MAX_WIDTH);
        titleLabel.setFont(selected ? FontThemeConstants.BOLD_12 : FontThemeConstants.REGULAR_12);
        return titleLabel;
    }

    private Node createNodeGraphic() {
        if (imageUrl != null) {
            // Create image view
            Image image = ImageCache.getImage(imageUrl);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(NAVIGATION_ICON_SIZE);
            imageView.setFitHeight(NAVIGATION_ICON_SIZE);
            return imageView;
        } else if (icon != null) {
            // Configure icon
            icon.setIconSize((int) NAVIGATION_ICON_SIZE);

            if (!selected) {
                icon.setIconColor(ColorThemeConstants.getGrey800());
            } else {
                icon.setIconColor(ColorThemeConstants.getMain950());
            }

            return icon;
        }
        // Create empty region as fallback
        Region emptyRegion = new Region();
        emptyRegion.setMinSize(NAVIGATION_ICON_SIZE, NAVIGATION_ICON_SIZE);
        emptyRegion.setPrefSize(NAVIGATION_ICON_SIZE, NAVIGATION_ICON_SIZE);
        return emptyRegion;
    }

    private Label createBadgeLabel(int count) {
        Label numberLabel = new Label(String.valueOf(count));
        numberLabel.setTextFill(ColorThemeConstants.getMain950());
        numberLabel.setFont(FontThemeConstants.BOLD_10);
        return numberLabel;
    }
}
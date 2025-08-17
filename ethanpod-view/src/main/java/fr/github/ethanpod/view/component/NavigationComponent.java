package fr.github.ethanpod.view.component;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.FontThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import static fr.github.ethanpod.view.util.Constant.*;

public class NavigationComponent {
    private static final Background SELECTED_BG = new Background(
            new BackgroundFill(ColorThemeConstants.getMain100(), new CornerRadii(2.0), null)
    );
    private static final Background TRANSPARENT_BG = new Background(
            new BackgroundFill(Color.TRANSPARENT, null, null)
    );
    private static final Background HOVER_BG = new Background(
            new BackgroundFill(ColorThemeConstants.getMain050(), null, null)
    );

    public static void updateAppearance(HBox navBox, boolean selected, boolean hover) {

        HBox iconTitleBox = (HBox) navBox.getChildren().getFirst();
        Node icon = iconTitleBox.getChildren().get(0);
        Label titleLabel = (Label) iconTitleBox.getChildren().get(1);

        if (hover && !selected) {
            navBox.setBackground(HOVER_BG);
        } else {
            navBox.setBackground(selected ? SELECTED_BG : TRANSPARENT_BG);
        }

        if (selected) {
            titleLabel.setTextFill(ColorThemeConstants.getMain950());
            titleLabel.setFont(FontThemeConstants.BOLD_12);
            if (icon instanceof FontIcon fontIcon) {
                fontIcon.setIconColor(ColorThemeConstants.getMain950());
            }
        } else {
            titleLabel.setTextFill(ColorThemeConstants.getGrey800());
            titleLabel.setFont(FontThemeConstants.REGULAR_12);
            if (icon instanceof FontIcon fontIcon) {
                fontIcon.setIconColor(ColorThemeConstants.getGrey800());
            }
        }
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, PRIORITY_ALWAYS);
        return spacer;
    }

    public HBox createNavigationCard(NavigationItem item) {
        HBox mainBox = new HBox();
        mainBox.setPadding(NAVIGATION_PADDING);
        mainBox.setMaxWidth(NAVIGATION_MAX_WIDTH);
        mainBox.setAlignment(POS_CENTER);

        // Icône/Image - création directe
        Node graphic;
        if (item.isIcon()) {
            FontIcon icon = new FontIcon(item.getName());
            icon.setIconSize((int) NAVIGATION_ICON_SIZE);
            icon.setIconColor(item.isSelected() ?
                    ColorThemeConstants.getMain950() : ColorThemeConstants.getGrey800());
            graphic = icon;
        } else {
            ImageView img = new ImageView(ImageCache.getImage(item.getName()));
            img.setFitWidth(NAVIGATION_ICON_SIZE);
            img.setFitHeight(NAVIGATION_ICON_SIZE);
            graphic = img;
        }

        // Label titre
        Label titleLabel = new Label(item.getTitle());
        titleLabel.setMaxWidth(NAVIGATION_TITLE_MAX_WIDTH);
        titleLabel.setFont(item.isSelected() ? FontThemeConstants.BOLD_12 : FontThemeConstants.REGULAR_12);

        // Container icône+titre - configuration directe
        HBox iconTitleBox = new HBox(NAVIGATION_SPACING, graphic, titleLabel);
        iconTitleBox.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        iconTitleBox.setAlignment(POS_CENTER);

        // Assemblage avec spacer et badge éventuel
        mainBox.getChildren().add(iconTitleBox);
        mainBox.getChildren().add(createSpacer());

        if (item.getNumber() > 0) {
            Label badge = new Label(String.valueOf(item.getNumber()));
            badge.setTextFill(ColorThemeConstants.getMain950());
            badge.setFont(FontThemeConstants.BOLD_10);
            mainBox.getChildren().add(badge);
        }

        mainBox.setBackground(item.isSelected() ? SELECTED_BG : TRANSPARENT_BG);

        return mainBox;
    }
}

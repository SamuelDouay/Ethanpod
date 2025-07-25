package fr.github.ethanpod.view.component.episode;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

import static fr.github.ethanpod.view.util.Constant.*;
import static fr.github.ethanpod.view.util.FontThemeConstants.MEDIUM_14;

public class EpisodeBuilder {
    private EpisodeItem episodeItem;
    private Button button;
    private HBox badge;

    public EpisodeBuilder() {
        // no parameter
    }

    public EpisodeBuilder withEpisodeItem(EpisodeItem episodeItem) {
        this.episodeItem = episodeItem;
        return this;
    }

    public EpisodeBuilder withButton(Button button) {
        this.button = button;
        return this;
    }

    public EpisodeBuilder withBadge(HBox badge) {
        this.badge = badge;
        return this;
    }

    public HBox build() {
        if (episodeItem == null) {
            throw new IllegalStateException("Episode item must be specified");
        }

        HBox box = new HBox(EPISODE_BOX_GAP);
        Region region = new Region();
        HBox.setHgrow(region, PRIORITY_ALWAYS);

        box.getChildren().add(createFirstPart());
        box.getChildren().add(region);
        box.getChildren().add(createSecondPart());

        setOnMouseHandler(box);

        HBox.setHgrow(box, PRIORITY_ALWAYS);
        box.setPadding(EPISODE_BOX_PADDING);
        box.setAlignment(POS_CENTER);

        return box;
    }

    private HBox createFirstPart() {
        HBox box = new HBox();
        box.setSpacing(EPISODE_FIRST_BOX_GAP);

        FontIcon icon = createFavoriteIcon();

        ImageView image = new ImageView(ImageCache.getImage(episodeItem.getUrlImage()));
        image.setFitHeight(EPISODE_IMAGE_SIZE);
        image.setFitWidth(EPISODE_IMAGE_SIZE);

        Label title = new Label(episodeItem.getName());
        title.setMinWidth(EPISODE_TITLE_SIZE);
        title.setMaxWidth(EPISODE_TITLE_SIZE);

        if (episodeItem.isRead()) {
            title.setTextFill(ColorThemeConstants.getGrey800());
        } else {
            title.setTextFill(ColorThemeConstants.getGrey950());
        }
        title.setFont(MEDIUM_14);

        box.getChildren().addAll(icon, image, title);
        box.setAlignment(POS_CENTER);

        return box;
    }

    private FontIcon createFavoriteIcon() {
        FontIcon icon;

        if (episodeItem.isFavorite()) {
            icon = new FontIcon(MaterialDesignS.STAR);
            icon.setIconColor(ColorThemeConstants.getMain700());
        } else {
            icon = new FontIcon(MaterialDesignS.STAR_OUTLINE);
            icon.setIconColor(ColorThemeConstants.getGrey900());
        }

        icon.setIconSize(EPISODE_ICON_SIZE);
        return icon;
    }

    private HBox createSecondPart() {
        HBox box = new HBox();

        // Add a badge
        box.getChildren().add(badge);

        // Add date label with spacer
        addLabelWithSpacer(box, episodeItem.getDate());

        // Add size label with spacer
        addLabelWithSpacer(box, episodeItem.getSize());

        // Add duration label with spacer
        addLabelWithSpacer(box, episodeItem.getDuration());

        // Add download button with spacer
        addSpacer(box);
        box.getChildren().add(button);

        // Add menu icon with spacer
        addSpacer(box);
        FontIcon menuIcon = new FontIcon(MaterialDesignD.DOTS_VERTICAL);
        menuIcon.setIconSize(EPISODE_ICON_SIZE);
        if (episodeItem.isRead()) {
            menuIcon.setIconColor(ColorThemeConstants.getGrey800());
        } else {
            menuIcon.setIconColor(ColorThemeConstants.getGrey950());
        }
        box.getChildren().add(menuIcon);

        box.setAlignment(POS_CENTER);
        HBox.setHgrow(box, PRIORITY_ALWAYS);

        return box;
    }

    private void addLabelWithSpacer(HBox container, String text) {
        addSpacer(container);
        Label label = createLabel(text);
        container.getChildren().addAll(label);
    }

    private void addSpacer(HBox container) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, PRIORITY_ALWAYS);
        container.getChildren().add(spacer);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        if (episodeItem.isRead()) {
            label.setTextFill(ColorThemeConstants.getGrey800());
        } else {
            label.setTextFill(ColorThemeConstants.getGrey950());
        }
        return label;
    }

    private void setOnMouseHandler(HBox box) {
        box.setOnMouseEntered(_ -> box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain050(), null, null))));
        box.setOnMouseExited(_ -> box.setBackground(null));
    }
}
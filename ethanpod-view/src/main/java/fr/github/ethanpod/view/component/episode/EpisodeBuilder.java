package fr.github.ethanpod.view.component.episode;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

public class EpisodeBuilder {
    private EpisodeItem episodeItem;
    private Button button;
    private HBox badge;

    /**
     * Create a new EpisodeBuilder
     */
    public EpisodeBuilder() {
        // no parameter
    }

    /**
     * Set the episode item to build a component for
     *
     * @param episodeItem The episode details
     * @return This builder instance for chaining
     */
    public EpisodeBuilder withEpisodeItem(EpisodeItem episodeItem) {
        this.episodeItem = episodeItem;
        return this;
    }

    /**
     * Set the download button text
     *
     * @param button The text for the download button
     * @return This builder instance for chaining
     */
    public EpisodeBuilder withButton(Button button) {
        this.button = button;
        return this;
    }

    /**
     * Set the download button text
     *
     * @param badge The text for the download button
     * @return This builder instance for chaining
     */
    public EpisodeBuilder withBadge(HBox badge) {
        this.badge = badge;
        return this;
    }

    /**
     * Build and return the episode component
     *
     * @return The constructed episode component as an HBox
     */
    public HBox build() {
        if (episodeItem == null) {
            throw new IllegalStateException("Episode item must be specified");
        }

        HBox box = new HBox(8.0);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        box.getChildren().add(createFirstPart());
        box.getChildren().add(region);
        box.getChildren().add(createSecondPart());

        setOnMouseHandler(box);

        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPadding(new Insets(8.0, 16.0, 8.0, 16.0));
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private HBox createFirstPart() {
        HBox box = new HBox();
        box.setSpacing(16.0);

        FontIcon icon = createFavoriteIcon();

        ImageView image = new ImageView(ImageCache.getImage(episodeItem.getUrlImage()));
        image.setFitHeight(40.0);
        image.setFitWidth(40.0);

        Label title = new Label(episodeItem.getName());
        title.setMinWidth(500.0);
        title.setMaxWidth(500.0);

        if (episodeItem.isRead()) {
            title.setTextFill(ColorThemeConstants.getGrey800());
        } else {
            title.setTextFill(ColorThemeConstants.getGrey950());
        }
        title.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        box.getChildren().addAll(icon, image, title);
        box.setAlignment(Pos.CENTER);

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

        icon.setIconSize(15);
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
        menuIcon.setIconSize(15);
        if (episodeItem.isRead()) {
            menuIcon.setIconColor(ColorThemeConstants.getGrey800());
        } else {
            menuIcon.setIconColor(ColorThemeConstants.getGrey950());
        }
        box.getChildren().add(menuIcon);

        box.setAlignment(Pos.CENTER);
        HBox.setHgrow(box, Priority.ALWAYS);

        return box;
    }

    private void addLabelWithSpacer(HBox container, String text) {
        addSpacer(container);
        Label label = createLabel(text);
        container.getChildren().addAll(label);
    }

    private void addSpacer(HBox container) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
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
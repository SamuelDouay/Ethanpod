package fr.github.ethanpod.view.component.surprise;

import com.podcast.antennapod.view.util.ColorThemeConstants;
import com.podcast.antennapod.view.util.ImageCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;

public class SurpriseBuilder {
    private String imageUrl;
    private String episodeTitle;
    private String podcastTitle;

    public SurpriseBuilder() {
        // no parameter
    }

    /**
     * Set the button text
     *
     * @param episodeTitle The text to display on the button
     * @return This builder instance for chaining
     */
    public SurpriseBuilder withEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
        return this;
    }

    /**
     * Set the button text
     *
     * @param podcastTitle The text to display on the button
     * @return This builder instance for chaining
     */
    public SurpriseBuilder withPodcastTitle(String podcastTitle) {
        this.podcastTitle = podcastTitle;
        return this;
    }


    /**
     * Set the button text
     *
     * @param imageUrl The text to display on the button
     * @return This builder instance for chaining
     */
    public SurpriseBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }


    public HBox build() {
        HBox box = getContainer();

        ImageView imageView = new ImageView(ImageCache.getImage(imageUrl));
        imageView.setFitHeight(45.0);
        imageView.setFitWidth(45.0);

        FontIcon icon = new FontIcon(MaterialDesignD.DOTS_VERTICAL);
        icon.setIconSize(15);

        box.getChildren().add(imageView);
        addSpacer(box);
        box.getChildren().add(getTextComponent());
        addSpacer(box);
        box.getChildren().add(icon);

        return box;
    }

    private void addSpacer(HBox container) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        container.getChildren().add(spacer);
    }

    private HBox getContainer() {
        HBox box = new HBox();

        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPadding(new Insets(8.0, 16.0, 8.0, 16.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain500(), null, null)));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox getTextComponent() {
        VBox box = new VBox();
        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setSpacing(8.0);

        Label labelTitle = new Label(episodeTitle);
        labelTitle.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));
        labelTitle.setTextFill(ColorThemeConstants.getMain950());
        labelTitle.setMaxWidth(250.0);


        Label labelPodcast = new Label(podcastTitle);
        labelPodcast.setFont(Font.font("Inter", FontPosture.REGULAR, 10));
        labelPodcast.setTextFill(ColorThemeConstants.getGrey900());
        labelPodcast.setMaxWidth(150.0);

        box.getChildren().addAll(labelTitle, labelPodcast);

        return box;
    }
}

package fr.github.ethanpod.view.component;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;

import static fr.github.ethanpod.view.util.Constant.*;
import static fr.github.ethanpod.view.util.FontThemeConstants.MEDIUM_14;
import static fr.github.ethanpod.view.util.FontThemeConstants.REGULAR_10;

public class SurpriseComponent {
    private static SurpriseComponent Instance;
    private final Background containerBg = new Background(new BackgroundFill(ColorThemeConstants.getMain500(), null, null));

    private SurpriseComponent() {
        //  no parameters
    }

    public static SurpriseComponent getInstance() {
        if (Instance == null) {
            Instance = new SurpriseComponent();
        }
        return Instance;
    }

    public HBox createSurprise(String imageUrl, String episodeTitle, String podcastTitle) {
        HBox box = getContainer();

        ImageView imageView = new ImageView(ImageCache.getImage(imageUrl));
        imageView.setFitHeight(SURPRISE_IMAGE_SIZE);
        imageView.setFitWidth(SURPRISE_IMAGE_SIZE);

        FontIcon icon = new FontIcon(MaterialDesignD.DOTS_VERTICAL);
        icon.setIconSize(SURPRISE_ICON_SIZE);

        box.getChildren().add(imageView);
        addSpacer(box);
        box.getChildren().add(getTextComponent(episodeTitle, podcastTitle));
        addSpacer(box);
        box.getChildren().add(icon);

        return box;
    }

    private void addSpacer(HBox container) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, PRIORITY_ALWAYS);
        container.getChildren().add(spacer);
    }

    private HBox getContainer() {
        HBox box = new HBox();

        box.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        HBox.setHgrow(box, PRIORITY_ALWAYS);
        box.setPadding(SURPRISE_PADDING);
        box.setBackground(containerBg);
        box.setAlignment(POS_CENTER);
        return box;
    }

    private VBox getTextComponent(String episodeTitle, String podcastTitle) {
        VBox box = new VBox();

        box.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        box.setSpacing(SURPRISE_GAP_CONTAINER);

        Label labelTitle = new Label(episodeTitle);
        labelTitle.setFont(MEDIUM_14);
        labelTitle.setTextFill(ColorThemeConstants.getMain950());
        labelTitle.setMaxWidth(SURPRISE_TITLE_MAX_WIDTH);


        Label labelPodcast = new Label(podcastTitle);
        labelPodcast.setFont(REGULAR_10);
        labelPodcast.setTextFill(ColorThemeConstants.getGrey900());
        labelPodcast.setMaxWidth(SURPRISE_SUBTITLE_MAX_WIDTH);

        box.getChildren().addAll(labelTitle, labelPodcast);

        return box;
    }
}

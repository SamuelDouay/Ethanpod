package fr.github.ethanpod.view.component;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static fr.github.ethanpod.view.util.Constant.*;
import static fr.github.ethanpod.view.util.FontThemeConstants.*;

public class ImageBuilder {
    private String imageUrl;
    private String title;
    private String date;
    private int episodeCount;

    ImageBuilder() {
    }

    public ImageBuilder withImage(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ImageBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ImageBuilder withDate(String date) {
        this.date = date;
        return this;
    }

    public ImageBuilder withEpisodeCount(int count) {
        this.episodeCount = count;
        return this;
    }

    public Node build() {

        if (imageUrl == null) {
            throw new IllegalStateException("Image url must be specified");
        }
        // Charger l'image (avec cache)
        Image image = ImageCache.getImage(imageUrl);

        // Calculer la hauteur du contenu
        double contentHeight = PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT;
        if (title != null) contentHeight += PODCAST_CARD_CONTENT_HEIGHT;
        if (date != null) contentHeight += PODCAST_CARD_CONTENT_HEIGHT;

        // Créer le conteneur principal
        StackPane container = createContainer(contentHeight);

        // Ajouter les composants dans l'ordre z-index
        if (title != null)
            container.getChildren().addAll(createBlurredBackground(image, contentHeight), createColorOverlay(contentHeight), createContent(image));
        else
            container.getChildren().addAll(createBlurredBackground(image, contentHeight));

        // Ajouter le badge de compteur d'épisodes si nécessaire
        if (episodeCount > 0) {
            container.getChildren().add(createEpisodeCountBadge(episodeCount));
        }

        return container;
    }

    private StackPane createContainer(double contentHeight) {
        double containerWidth = PODCAST_CARD_WIDTH;
        double containerHeight = calculateTotalHeight(contentHeight);

        StackPane stackPane = new StackPane();
        stackPane.setMinSize(containerWidth, containerHeight);
        stackPane.setPrefSize(containerWidth, containerHeight);

        // Utiliser un clip pour assurer que le contenu ne déborde pas
        Rectangle clip = new Rectangle(containerWidth, containerHeight);
        stackPane.setClip(clip);

        stackPane.setAlignment(POS_CENTER);
        stackPane.setPadding(new Insets(PODCAST_CARD_DEFAULT_PADDING));

        return stackPane;
    }

    private ImageView createBlurredBackground(Image image, double height) {
        double totalWidth = PODCAST_CARD_WIDTH;
        double totalHeight = calculateTotalHeight(height);

        ImageView blurredBackground = new ImageView(image);
        blurredBackground.setFitHeight(totalHeight);
        blurredBackground.setFitWidth(totalWidth);

        if (title != null) {

            // Dimensionner l'image d'arrière-plan pour qu'elle couvre entièrement
            double scaleFactor = 1.2;
            blurredBackground.setFitWidth(totalWidth * scaleFactor);
            blurredBackground.setFitHeight(totalHeight * scaleFactor);
            // Centrer l'image agrandie
            blurredBackground.setTranslateX((totalWidth * scaleFactor - totalWidth) / -2);
            blurredBackground.setTranslateY((totalHeight * scaleFactor - totalHeight) / -2);
            // Appliquer l'effet de flou
            blurredBackground.setEffect(PODCAST_CARD_BACKGROUND_BLUR);
        }
        return blurredBackground;
    }

    private Rectangle createColorOverlay(double height) {
        Rectangle overlay = new Rectangle(PODCAST_CARD_WIDTH, calculateTotalHeight(height));
        overlay.setFill(PODCAST_CARD_OVERLAY_COLOR);
        return overlay;
    }

    private double calculateTotalHeight(double contentHeight) {
        return contentHeight + 2 * PODCAST_CARD_DEFAULT_PADDING;
    }

    private VBox createContent(Image image) {
        VBox content = new VBox(5);
        content.setAlignment(POS_TOP_LEFT);

        // Ajouter l'image principale
        content.getChildren().add(createMainImage(image));

        // Ajouter le texte si nécessaire
        if (title != null || date != null) {
            content.getChildren().add(createTextContent());
        }

        return content;
    }

    private ImageView createMainImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        imageView.setFitHeight(PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);  // Meilleure qualité d'image
        imageView.setCache(true);   // Activer le cache pour de meilleures performances
        return imageView;
    }

    private VBox createTextContent() {
        VBox textContainer = new VBox(2.0);
        textContainer.setAlignment(POS_BASELINE_LEFT);

        if (title != null) {
            textContainer.getChildren().add(createLabel(title, BOLD_15, ColorThemeConstants.getGrey100()));
        }

        if (date != null) {
            textContainer.getChildren().add(createLabel(date, REGULAR_10, ColorThemeConstants.getGrey100()));
        }

        return textContainer;
    }

    private Label createLabel(String text, Font font, Color color) {
        Label label = new Label(text);
        label.setTextFill(color);
        label.setFont(font);
        label.setWrapText(true);
        label.setMaxWidth(PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        label.setAlignment(POS_BASELINE_LEFT);

        // Optimisation pour éviter le recalcul de mise en page
        Text helper = new Text(text);
        helper.setFont(font);
        double preferredWidth = Math.min(helper.getLayoutBounds().getWidth(), PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        label.setPrefWidth(preferredWidth);

        return label;
    }

    private Node createEpisodeCountBadge(int count) {
        Label countLabel = new Label(String.valueOf(count));
        countLabel.setFont(MEDIUM_12);
        countLabel.setTextFill(ColorThemeConstants.getMain900());

        HBox badgeBox = new HBox();
        badgeBox.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain100(), PODCAST_CARD_BADGE_CORNER, INSETS_EMPTY)));
        badgeBox.setPadding(PODCAST_CARD_BADGE_BOX_PADDING);
        badgeBox.setAlignment(POS_CENTER);
        badgeBox.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        badgeBox.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        badgeBox.getChildren().add(countLabel);

        // Positionner le badge en haut à gauche
        StackPane.setAlignment(badgeBox, POS_TOP_LEFT);
        StackPane.setMargin(badgeBox, PODCAST_CARD_BADGE_PADDING);

        return badgeBox;
    }


}

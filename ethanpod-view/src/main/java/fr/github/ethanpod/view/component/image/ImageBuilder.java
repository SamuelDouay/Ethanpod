package fr.github.ethanpod.view.component.image;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.Constant;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ImageBuilder {
    private static final String FONT = "Inter";
    private static final double PADDING = Constant.PODCAST_CARD_DEFAULT_PADDING;
    private static final double IMAGE_SIZE = Constant.PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT;
    private static final double WIDTH = IMAGE_SIZE + 2 * PADDING;
    private final BoxBlur backgroundBlur = new BoxBlur(200, 200, 5);
    private final Color overlayColor = Color.hsb(230.0, 0.17, 0.14, 0.2);
    private final Font titleFont = Font.font(FONT, FontWeight.BOLD, 15);
    private final Font dateFont = Font.font(FONT, FontPosture.REGULAR, 10);
    private final Font countFont = Font.font(FONT, FontWeight.MEDIUM, 12);
    private final CornerRadii badgeCorner = new CornerRadii(99.0);
    private final Insets badgePadding = new Insets(2.0, 7.0, 2.0, 7.0);
    private final Insets badgeMargin = new Insets(10, 0, 0, 10);
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
        double contentHeight = IMAGE_SIZE;
        if (title != null) contentHeight += 25.0;
        if (date != null) contentHeight += 25.0;

        // Créer le conteneur principal
        StackPane container = createContainer(contentHeight);

        // Ajouter les composants dans l'ordre z-index
        container.getChildren().addAll(createBlurredBackground(image, contentHeight), createColorOverlay(contentHeight), createContent(image));

        // Ajouter le badge de compteur d'épisodes si nécessaire
        if (episodeCount > 0) {
            container.getChildren().add(createEpisodeCountBadge(episodeCount));
        }

        return container;
    }

    private StackPane createContainer(double contentHeight) {
        double containerWidth = WIDTH;
        double containerHeight = calculateTotalHeight(contentHeight);

        StackPane stackPane = new StackPane();
        stackPane.setMinSize(containerWidth, containerHeight);
        stackPane.setPrefSize(containerWidth, containerHeight);

        // Utiliser un clip pour assurer que le contenu ne déborde pas
        Rectangle clip = new Rectangle(containerWidth, containerHeight);
        stackPane.setClip(clip);

        stackPane.setAlignment(Pos.CENTER);
        stackPane.setPadding(new Insets(PADDING));

        return stackPane;
    }

    private ImageView createBlurredBackground(Image image, double height) {
        double totalWidth = WIDTH;
        double totalHeight = calculateTotalHeight(height);

        ImageView blurredBackground = new ImageView(image);

        // Dimensionner l'image d'arrière-plan pour qu'elle couvre entièrement
        double scaleFactor = 1.2;
        blurredBackground.setFitWidth(totalWidth * scaleFactor);
        blurredBackground.setFitHeight(totalHeight * scaleFactor);

        // Centrer l'image agrandie
        blurredBackground.setTranslateX((totalWidth * scaleFactor - totalWidth) / -2);
        blurredBackground.setTranslateY((totalHeight * scaleFactor - totalHeight) / -2);

        // Appliquer l'effet de flou
        blurredBackground.setEffect(backgroundBlur);

        return blurredBackground;
    }

    private Rectangle createColorOverlay(double height) {
        Rectangle overlay = new Rectangle(WIDTH, calculateTotalHeight(height));
        overlay.setFill(overlayColor);
        return overlay;
    }

    private double calculateTotalHeight(double contentHeight) {
        return contentHeight + 2 * PADDING;
    }

    private VBox createContent(Image image) {
        VBox content = new VBox(5);
        content.setAlignment(Pos.TOP_LEFT);

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
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);  // Meilleure qualité d'image
        imageView.setCache(true);   // Activer le cache pour de meilleures performances
        return imageView;
    }

    private VBox createTextContent() {
        VBox textContainer = new VBox(2.0);
        textContainer.setAlignment(Pos.BASELINE_LEFT);

        if (title != null) {
            textContainer.getChildren().add(createLabel(title, titleFont, ColorThemeConstants.getGrey100()));
        }

        if (date != null) {
            textContainer.getChildren().add(createLabel(date, dateFont, ColorThemeConstants.getGrey100()));
        }

        return textContainer;
    }

    private Label createLabel(String text, Font font, Color color) {
        Label label = new Label(text);
        label.setTextFill(color);
        label.setFont(font);
        label.setWrapText(true);
        label.setMaxWidth(IMAGE_SIZE);
        label.setAlignment(Pos.BASELINE_LEFT);

        // Optimisation pour éviter le recalcul de mise en page
        Text helper = new Text(text);
        helper.setFont(font);
        double preferredWidth = Math.min(helper.getLayoutBounds().getWidth(), IMAGE_SIZE);
        label.setPrefWidth(preferredWidth);

        return label;
    }

    private Node createEpisodeCountBadge(int count) {
        Label countLabel = new Label(String.valueOf(count));
        countLabel.setFont(countFont);
        countLabel.setTextFill(ColorThemeConstants.getMain900());

        HBox badgeBox = new HBox();
        badgeBox.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain100(), badgeCorner, Insets.EMPTY)));
        badgeBox.setPadding(badgePadding);
        badgeBox.setAlignment(Pos.CENTER);
        badgeBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        badgeBox.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        badgeBox.getChildren().add(countLabel);

        // Positionner le badge en haut à gauche
        StackPane.setAlignment(badgeBox, Pos.TOP_LEFT);
        StackPane.setMargin(badgeBox, badgeMargin);

        return badgeBox;
    }


}

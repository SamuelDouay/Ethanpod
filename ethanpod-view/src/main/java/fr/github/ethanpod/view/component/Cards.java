package fr.github.ethanpod.view.component;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import static fr.github.ethanpod.view.util.Constant.*;
import static fr.github.ethanpod.view.util.FontThemeConstants.*;

public final class Cards {

    // Padding du container — objet constant, pas recréé à chaque carte
    private static final Insets CARD_PADDING = new Insets(PODCAST_CARD_DEFAULT_PADDING);

    // Background du badge de compteur — reconstruit au changement de thème
    private static Background badgeBg = buildBadgeBg();

    static {
        ColorThemeConstants.addThemeChangeListener(t -> badgeBg = buildBadgeBg());
    }

    private Cards() {
    }

    public static Node image(String url) {
        if (url == null) throw new IllegalArgumentException("url cannot be null");

        Image image = loadImage(url);
        double totalHeight = totalHeight(false, false);

        StackPane card = buildContainer(totalHeight);
        card.getChildren().add(buildBackground(image, totalHeight, false));
        return card;
    }

    public static Node image(String url, String title, int episodeCount) {
        if (url == null) throw new IllegalArgumentException("url cannot be null");

        Image image = loadImage(url);
        // Le compteur n'ajoute pas de hauteur — il est superposé en badge
        double totalHeight = totalHeight(title != null, false);

        StackPane card = buildContainer(totalHeight);
        card.getChildren().addAll(
                buildBackground(image, totalHeight, title != null),
                buildOverlay(totalHeight),
                buildContent(image, title, null)
        );
        if (episodeCount > 0) {
            card.getChildren().add(buildCountBadge(episodeCount));
        }
        return card;
    }

    public static Node image(String url, String title, String date) {
        if (url == null) throw new IllegalArgumentException("url cannot be null");

        Image image = loadImage(url);
        double totalHeight = totalHeight(title != null, date != null);

        StackPane card = buildContainer(totalHeight);
        card.getChildren().addAll(
                buildBackground(image, totalHeight, title != null),
                buildOverlay(totalHeight),
                buildContent(image, title, date)
        );
        return card;
    }

    private static double totalHeight(boolean hasTitle, boolean hasDate) {
        double content = PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT;
        if (hasTitle) content += PODCAST_CARD_CONTENT_HEIGHT;
        if (hasDate) content += PODCAST_CARD_CONTENT_HEIGHT;
        return content + 2 * PODCAST_CARD_DEFAULT_PADDING;
    }

    private static StackPane buildContainer(double totalHeight) {
        StackPane pane = new StackPane();
        pane.setMinSize(PODCAST_CARD_WIDTH, totalHeight);
        pane.setPrefSize(PODCAST_CARD_WIDTH, totalHeight);
        pane.setAlignment(POS_CENTER);
        pane.setPadding(CARD_PADDING);
        pane.setClip(new Rectangle(PODCAST_CARD_WIDTH, totalHeight));
        return pane;
    }

    private static ImageView buildBackground(Image image, double totalHeight, boolean withBlur) {
        ImageView bg = new ImageView(image);

        if (withBlur) {
            double scaledW = PODCAST_CARD_WIDTH * 1.2;
            double scaledH = totalHeight * 1.2;
            bg.setFitWidth(scaledW);
            bg.setFitHeight(scaledH);
            // Centrage de l'image agrandie
            bg.setTranslateX((scaledW - PODCAST_CARD_WIDTH) / -2.0);
            bg.setTranslateY((scaledH - totalHeight) / -2.0);
            bg.setEffect(PODCAST_CARD_BACKGROUND_BLUR);
        } else {
            bg.setFitWidth(PODCAST_CARD_WIDTH);
            bg.setFitHeight(totalHeight);
        }
        return bg;
    }

    private static Rectangle buildOverlay(double totalHeight) {
        Rectangle overlay = new Rectangle(PODCAST_CARD_WIDTH, totalHeight);
        overlay.setFill(PODCAST_CARD_OVERLAY_COLOR);
        return overlay;
    }

    private static VBox buildContent(Image image, String title, String date) {
        VBox content = new VBox(5);
        content.setAlignment(POS_TOP_LEFT);
        content.getChildren().add(buildMainImage(image));

        if (title != null || date != null) {
            VBox textBlock = new VBox(2.0);
            textBlock.setAlignment(POS_BASELINE_LEFT);

            if (title != null) textBlock.getChildren().add(buildLabel(title, BOLD_15));
            if (date != null) textBlock.getChildren().add(buildLabel(date, REGULAR_10));

            content.getChildren().add(textBlock);
        }
        return content;
    }

    private static ImageView buildMainImage(Image image) {
        ImageView iv = new ImageView(image);
        iv.setFitWidth(PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        iv.setFitHeight(PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        return iv;
    }

    private static Label buildLabel(String text, javafx.scene.text.Font font) {
        Label label = new Label(text);
        label.setTextFill(ColorThemeConstants.getGrey100());
        label.setFont(font);
        label.setWrapText(true);
        label.setMaxWidth(PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT);
        label.setAlignment(POS_BASELINE_LEFT);
        return label;
    }

    private static Node buildCountBadge(int count) {
        Label countLabel = new Label(String.valueOf(count));
        countLabel.setFont(MEDIUM_12);
        countLabel.setTextFill(ColorThemeConstants.getMain900());

        HBox badge = new HBox();
        badge.setBackground(badgeBg);
        badge.setPadding(PODCAST_CARD_BADGE_BOX_PADDING);
        badge.setAlignment(POS_CENTER);
        badge.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        badge.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        badge.getChildren().add(countLabel);

        StackPane.setAlignment(badge, POS_TOP_LEFT);
        StackPane.setMargin(badge, PODCAST_CARD_BADGE_PADDING);
        return badge;
    }


    private static Image loadImage(String url) {
        return ImageCache.getImage(url);
    }

    private static Background buildBadgeBg() {
        return new Background(new BackgroundFill(
                ColorThemeConstants.getMain100(),
                PODCAST_CARD_BADGE_CORNER,
                INSETS_EMPTY));
    }
}
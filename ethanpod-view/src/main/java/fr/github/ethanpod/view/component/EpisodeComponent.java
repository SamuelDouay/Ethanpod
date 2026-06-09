package fr.github.ethanpod.view.component;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

import static fr.github.ethanpod.view.util.Constant.*;
import static fr.github.ethanpod.view.util.FontThemeConstants.MEDIUM_14;

public class EpisodeComponent {
    private static EpisodeComponent INSTANCE;

    private Background hoverBg = buildHoverBg();

    private EpisodeComponent() {
        // no parameters
    }

    public static EpisodeComponent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EpisodeComponent();
        }
        return INSTANCE;
    }

    private Background buildHoverBg() {
        return new Background(new BackgroundFill(ColorThemeConstants.getMain050(), null, null));
    }

    public HBox createEpisode(EpisodeItem episodeItem) {
        if (episodeItem == null) {
            throw new IllegalStateException("Episode item must be specified");
        }

        HBox firstPart = createFirstPart(episodeItem);
        Region spacer = growingSpacer();
        HBox secondPart = createSecondPart(episodeItem);

        HBox box = new HBox(EPISODE_BOX_GAP);
        box.getChildren().addAll(firstPart, spacer, secondPart);

        setOnMouseHandler(box);

        HBox.setHgrow(box, PRIORITY_ALWAYS);
        box.setPadding(EPISODE_BOX_PADDING);
        box.setAlignment(POS_CENTER);

        return box;
    }


    private HBox createFirstPart(EpisodeItem episodeItem) {
        FontIcon starIcon = buildStarIcon(episodeItem);

        ImageView image = new ImageView(ImageCache.getImage(episodeItem.getUrlImage()));
        image.setFitHeight(EPISODE_IMAGE_SIZE);
        image.setFitWidth(EPISODE_IMAGE_SIZE);

        Label title = new Label(episodeItem.getTitle());
        title.setMinWidth(EPISODE_TITLE_SIZE);
        title.setMaxWidth(EPISODE_TITLE_SIZE);
        title.setFont(MEDIUM_14);

        title.setTextFill(episodeItem.isRead()
                ? ColorThemeConstants.getGrey800()
                : ColorThemeConstants.getGrey950());

        HBox box = new HBox(EPISODE_FIRST_BOX_GAP);
        box.getChildren().addAll(starIcon, image, title);
        box.setAlignment(POS_CENTER);
        return box;
    }

    private FontIcon buildStarIcon(EpisodeItem episodeItem) {
        boolean read = episodeItem.isRead();
        FontIcon icon;

        if (episodeItem.isFavorite()) {
            icon = new FontIcon(MaterialDesignS.STAR_OUTLINE);
            icon.setIconColor(read
                    ? ColorThemeConstants.getMain700()
                    : ColorThemeConstants.getGrey950());

        } else {
            icon = new FontIcon(MaterialDesignS.STAR_OUTLINE);
            icon.setIconColor(read
                    ? ColorThemeConstants.getMain700()
                    : ColorThemeConstants.getGrey950());

        }

        icon.setIconSize(EPISODE_ICON_SIZE);
        return icon;
    }

    private HBox createSecondPart(EpisodeItem episodeItem) {
        boolean read = episodeItem.isRead();
        var textColor = read
                ? ColorThemeConstants.getGrey800()
                : ColorThemeConstants.getGrey950();

        HBox box = new HBox();
        box.setAlignment(POS_CENTER);
        HBox.setHgrow(box, PRIORITY_ALWAYS);

        if (episodeItem.isInInbox())
            box.getChildren().add(Badges.blue(new FontIcon(MaterialDesignI.INBOX)));
        if (episodeItem.isInQueue())
            box.getChildren().add(Badges.purple(new FontIcon(MaterialDesignP.PLAYLIST_PLAY)));

        addSpacedLabel(box, episodeItem.getDate(), textColor);
        addSpacedLabel(box, episodeItem.getSize(), textColor);
        addSpacedLabel(box, episodeItem.getDuration(), textColor);

        box.getChildren().add(growingSpacer());
        box.getChildren().add(Buttons.primary("Download"));

        box.getChildren().add(growingSpacer());
        FontIcon menuIcon = new FontIcon(MaterialDesignD.DOTS_VERTICAL);
        menuIcon.setIconSize(EPISODE_ICON_SIZE);
        menuIcon.setIconColor(textColor); // même couleur que les labels
        box.getChildren().add(menuIcon);


        return box;
    }


    private void addSpacedLabel(HBox container, String text, javafx.scene.paint.Color color) {
        container.getChildren().add(growingSpacer());
        Label label = new Label(text);
        label.setTextFill(color);
        container.getChildren().add(label);
    }

    private Region growingSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, PRIORITY_ALWAYS);
        return spacer;
    }


    private Label createLabel(String text, EpisodeItem episodeItem) {
        Label label = new Label(text);
        if (episodeItem.isRead()) {
            label.setTextFill(ColorThemeConstants.getGrey800());
        } else {
            label.setTextFill(ColorThemeConstants.getGrey950());
        }
        return label;
    }

    private void setOnMouseHandler(HBox box) {
        box.setOnMouseEntered(_ -> box.setBackground(hoverBg));
        box.setOnMouseExited(_ -> box.setBackground(null));
    }
}

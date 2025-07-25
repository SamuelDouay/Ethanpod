package fr.github.ethanpod.view.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Constant {
    // GENERAL
    public static final double USE_PREF_SIZE = Region.USE_PREF_SIZE;
    public static final Pos POS_CENTER = Pos.CENTER;
    public static final Pos POS_TOP_LEFT = Pos.TOP_LEFT;
    public static final Pos POS_BASELINE_LEFT = Pos.BASELINE_LEFT;
    public static final Priority PRIORITY_ALWAYS = Priority.ALWAYS;
    public static final Insets INSETS_EMPTY = Insets.EMPTY;
    public static final CornerRadii DEFAULT_CORNER_RADII = new CornerRadii(2.0);

    // BUTTON
    public static final int BUTTON_ICON_ONLY_SIZE = 40;
    public static final Insets BUTTON_ICON_ONLY_PADDING = new Insets(8);
    public static final int BUTTON_ICON_SIZE = 20;
    public static final Insets BUTTON_DEFAULT_PADDING = new Insets(8.0, 16.0, 8.0, 16.0);
    public static final Insets BUTTON_SECONDARY_PADDING = new Insets(6.0, 14.0, 6.0, 14.0);
    public static final CornerRadii BUTTON_CIRCLE_RADII = new CornerRadii(20);
    public static final BorderWidths BUTTON_BORDER_WIDTH = new BorderWidths(2);

    // BADGE
    public static final double BADGE_DEFAULT_WIDTH = 100.0;
    public static final Insets BADGE_DEFAULT_PADDING = new Insets(4.0, 16.0, 4.0, 16.0);
    public static final Insets BADGE_ICON_PADDING = new Insets(4.0);
    public static final int BADGE_ICON_SIZE = 15;
    public static final int BADGE_TEXT_GAP = 5;

    // PODCAST CARD
    public static final double PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT = 140.0;
    public static final double PODCAST_CARD_DEFAULT_PADDING = 12.0;
    public static final double PODCAST_CARD_WIDTH = PODCAST_CARD_DEFAULT_IMAGE_WIDTH_HEIGHT + 2 * PODCAST_CARD_DEFAULT_PADDING;
    public static final BoxBlur PODCAST_CARD_BACKGROUND_BLUR = new BoxBlur(200, 200, 5);
    public static final Color PODCAST_CARD_OVERLAY_COLOR = Color.hsb(230.0, 0.17, 0.14, 0.2);
    public static final CornerRadii PODCAST_CARD_BADGE_CORNER = new CornerRadii(99.0);
    public static final Insets PODCAST_CARD_BADGE_BOX_PADDING = new Insets(2.0, 7.0, 2.0, 7.0);
    public static final Insets PODCAST_CARD_BADGE_PADDING = new Insets(10, 0, 0, 10);
    public static final double PODCAST_CARD_CONTENT_HEIGHT = 25.0;

    // NAVIGATION
    public static final double NAVIGATION_ICON_SIZE = 25.0;
    public static final double NAVIGATION_TITLE_MAX_WIDTH = 140.0;
    public static final double NAVIGATION_MAX_WIDTH = 224.0;
    public static final double NAVIGATION_SPACING = 14.0;
    public static final Insets NAVIGATION_PADDING = new Insets(6.0, 12.0, 6.0, 12.0);

    // EPISODE
    public static final Insets EPISODE_BOX_PADDING = new Insets(8.0, 16.0, 8.0, 16.0);
    public static final double EPISODE_BOX_GAP = 8.0;
    public static final double EPISODE_FIRST_BOX_GAP = 16.0;
    public static final double EPISODE_IMAGE_SIZE = 40.0;
    public static final double EPISODE_TITLE_SIZE = 500.0;
    public static final int EPISODE_ICON_SIZE = 15;

    // SURPRISE
    public static final double SURPRISE_IMAGE_SIZE = 45.0;
    public static final int SURPRISE_ICON_SIZE = 15;
    public static final Insets SURPRISE_PADDING = new Insets(8.0, 16.0, 8.0, 16.0);
    public static final double SURPRISE_GAP_CONTAINER = 8.0;
    public static final double SURPRISE_TITLE_MAX_WIDTH = 250.0;
    public static final double SURPRISE_SUBTITLE_MAX_WIDTH = 150.0;

    private Constant() {

    }
}

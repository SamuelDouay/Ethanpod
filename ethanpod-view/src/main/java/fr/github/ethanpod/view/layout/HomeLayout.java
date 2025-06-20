package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.service.EpisodeService;
import fr.github.ethanpod.view.component.episode.EpisodeComponent;
import fr.github.ethanpod.view.component.image.ImageComponent;
import fr.github.ethanpod.view.component.surprise.SurpriseComponent;
import fr.github.ethanpod.view.layout.context.ContextualLayout;
import fr.github.ethanpod.view.layout.context.HomeContext;
import fr.github.ethanpod.view.layout.context.LayoutContext;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HomeLayout extends Layout implements ContextualLayout {
    public static final ImageComponent IMAGE_COMPONENT = new ImageComponent();
    public static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    public static final SurpriseComponent SURPRISE_COMPONENT = new SurpriseComponent();

    // Constants for image paths
    public static final String IMAGE_EX = String.valueOf(HomeLayout.class.getResource("/images/ex.jpeg"));
    public static final String IMAGE_HDM = String.valueOf(HomeLayout.class.getResource("/images/heure_du_monde.png"));
    public static final String IMAGE_SMLTLK = String.valueOf(HomeLayout.class.getResource("/images/small_talk.jpg"));
    public static final String IMAGE_UNDERSCORE = String.valueOf(HomeLayout.class.getResource("/images/underscore.jpeg"));
    public static final String IMAGE_ZERL = String.valueOf(HomeLayout.class.getResource("/images/zerl.jpg"));

    // Constants for example data
    public static final String TITLE_EXAMPLE = "Lil Nas X, une icône noire, et gay et flamboyante [REDIF]";
    public static final String TIME_EXAMPLE = "00:20:40";
    public static final String DATE_EXAMPLE = "28/10/2024";
    public static final String MO_EXAMPLE = "18 Mo";

    private VBox mainContainer;

    public HomeLayout() {
        super("Home");
    }

    private VBox getNewsSection() {
        VBox box = getMainBox();
        box.getChildren().add(getTitleSection("See what's news"));
        box.getChildren().add(getNewsTable());
        return box;
    }

    private VBox getMainBox() {
        VBox box = new VBox(12);
        box.setPrefWidth(Region.USE_PREF_SIZE);
        box.setMaxHeight(Region.USE_PREF_SIZE);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private VBox getSurpriseSection() {
        VBox box = getMainBox();
        box.getChildren().add(getTitleSection("Get surprised"));
        box.getChildren().add(getSurpriseTable());
        return box;
    }

    private VBox getDownloadSection() {
        VBox box = getMainBox();
        box.getChildren().add(getTitleSection("Manage downloads"));
        box.getChildren().add(getNewsTable());
        return box;
    }

    private VBox getClassicsSection() {
        VBox box = getMainBox();
        box.getChildren().add(getTitleSection("Check your classic"));
        box.getChildren().add(getClassic());
        return box;
    }

    private VBox getListeningSection() {
        VBox box = getMainBox();
        box.getChildren().add(getTitleSection("Continue listening"));
        box.getChildren().add(getListening());
        return box;
    }

    private Label getTitleSection(String title) {
        Label label = new Label(title);
        label.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        label.setTextFill(ColorThemeConstants.getMain950());
        return label;
    }

    private Node getSurpriseTable() {
        GridPane box = new GridPane();
        box.setVgap(15.0);
        box.setHgap(15.0);
        box.setPrefWidth(Region.USE_PREF_SIZE);
        box.setMaxHeight(Region.USE_PREF_SIZE);

        int numColumns = 3;
        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setPercentWidth(100.0 / numColumns);
            box.getColumnConstraints().add(column);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Node surpriseComponent = SURPRISE_COMPONENT.createSurprise(IMAGE_HDM, TITLE_EXAMPLE, "L'heure du monde");
                GridPane.setHalignment(surpriseComponent, HPos.CENTER);
                box.add(surpriseComponent, i, j, 1, 1);
            }
        }
        return box;
    }

    private Node getNewsTable() {
        VBox box = new VBox();

        EpisodeService episodeService = new EpisodeService();

        for (EpisodeItem e : episodeService.getNewsTop8()) {
            box.getChildren().add(EPISODE_COMPONENT.createInboxEpisode(e));
        }

        return box;
    }

    private ScrollPane getClassic() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        HBox box = new HBox(15);
        box.setPadding(new Insets(0.0, 1.0, 0.0, 1.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(box, Priority.ALWAYS);

        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_EX));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_HDM));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_SMLTLK));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_UNDERSCORE));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_ZERL));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_UNDERSCORE));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_ZERL));
        box.getChildren().add(IMAGE_COMPONENT.createImageCard(IMAGE_SMLTLK));

        scrollPane.setContent(box);
        return scrollPane;
    }

    private ScrollPane getListening() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        HBox box = new HBox(15);
        box.setPadding(new Insets(0.0, 1.0, 0.0, 1.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(box, Priority.ALWAYS);

        EpisodeService episodeService = new EpisodeService();

        for (EpisodeItem e : episodeService.getTop8Queue()) {
            box.getChildren().add(IMAGE_COMPONENT.createImageCard(e.getUrlImage(), e.getName(), e.getDate()));
        }

        scrollPane.setContent(box);
        return scrollPane;
    }

    @Override
    public VBox getLayout() {
        mainContainer = getContainer();
        buildLayout();
        return mainContainer;
    }

    private void buildLayout() {
        mainContainer.getChildren().clear();
        mainContainer.getChildren().addAll(
                getTitle(),
                getListeningSection(),
                getNewsSection(),
                getSurpriseSection(),
                getClassicsSection(),
                getDownloadSection()
        );
    }

    @Override
    public void updateContext(LayoutContext context) {
        // no
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return HomeContext.class.isAssignableFrom(contextType);
    }
}

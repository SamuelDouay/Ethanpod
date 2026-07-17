package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.item.*;
import fr.github.ethanpod.event.updated.PodcastByIdUpdated;
import fr.github.ethanpod.view.component.Cards;
import fr.github.ethanpod.view.component.EpisodeComponent;
import fr.github.ethanpod.view.component.SurpriseComponent;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class PageContentRenderer {
    private static final EpisodeComponent EPISODE_COMPONENT = EpisodeComponent.getInstance();
    private static final SurpriseComponent SURPRISE_COMPONENT = SurpriseComponent.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(PageContentRenderer.class);
    private static final Font SECTION_TITLE_FONT = Font.font("Inter", FontWeight.BOLD, 20);
    private static final Color SECTION_TITLE_COLOR = ColorThemeConstants.getMain950();
    private static final Background GREY_BG = new Background(
            new BackgroundFill(ColorThemeConstants.getGrey000(), null, null));
    private static final Insets HSCROLL_PADDING = new Insets(0.0, 1.0, 0.0, 1.0);
    private static final Set<Section> REQUIRED_SECTIONS = EnumSet.of(
            Section.QUEUE, Section.INBOX, Section.PODCAST, Section.DOWNLOAD);
    private final VBox container;
    private final FlowPane grid;
    private final Runnable gridInitializer;
    // Tracking des sections reçues
    private final Set<Section> sectionsReceived = EnumSet.noneOf(Section.class);
    // Variables pour les sections dans l'ordre fixe souhaité
    private VBox homeWrapper;
    private VBox queueBox;
    private VBox inboxBox;
    private VBox classicBox;
    private VBox downloadBox;
    private VBox surpriseBox;

    public PageContentRenderer(VBox container, FlowPane grid, Runnable gridInitializer) {
        this.container = container;
        this.grid = grid;
        this.gridInitializer = gridInitializer;
    }

    public void updatePodcastTitle(PodcastByIdUpdated event) {
        PodcastItem podcastItem = (PodcastItem) event.getItem();
        LOGGER.info("Getting podcast: {}", podcastItem.getTitle());

        VBox subtitle = new VBox();
        subtitle.getChildren().addAll(
                new Label(podcastItem.getAuthor()),
                new Label(podcastItem.getDescription())
        );
        container.getChildren().add(subtitle);
    }

    public void updateEpisodes(List<? extends Item> episodes, boolean append) {
        LOGGER.info("Getting {} episodes, append: {}", episodes.size(), append);

        if (!append) {
            container.getChildren().removeIf(node ->
                    node instanceof VBox vbox &&
                            (vbox.getChildren().isEmpty() || !(vbox.getChildren().getFirst() instanceof Label))
            );
        }

        List<Node> nodes = new ArrayList<>(episodes.size());
        for (Item episode : episodes) {
            nodes.add(EPISODE_COMPONENT.createEpisode((EpisodeItem) episode));
        }
        container.getChildren().addAll(nodes);
    }

    public void updateSubscriptions(List<? extends Item> items, boolean append) {
        if (!append) {
            initializeGrid();
            grid.getChildren().clear();
            container.getChildren().clear();
        } else {
            initializeGridIfNeeded();
        }

        List<Node> cards = new ArrayList<>(items.size());
        for (Item item : items) {
            NavigationItem navigationItem = (NavigationItem) item;
            cards.add(Cards.image(
                    navigationItem.getUrlImage(), navigationItem.getTitle(), navigationItem.getNumber()));
        }
        grid.getChildren().addAll(cards);

        if (!append && !container.getChildren().contains(grid)) {
            container.getChildren().add(grid);
        }
    }

    public void updateSurprise(List<? extends Item> items, GridPane box) {

        if (box.getColumnConstraints().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setHgrow(Priority.ALWAYS);
                col.setPercentWidth(100.0 / 3);
                box.getColumnConstraints().add(col);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SurpriseItem surpriseItem = (SurpriseItem) items.get(i * 3 + j);
                Node surpriseComponent = SURPRISE_COMPONENT.createSurprise(surpriseItem.getImageUrl(), surpriseItem.getTitle(), surpriseItem.getPodcastTitle());
                GridPane.setHalignment(surpriseComponent, HPos.CENTER);
                box.add(surpriseComponent, i, j, 1, 1);
            }
        }
    }

    public void updateHomePage() {
        // Vérifier que toutes les sections attendues sont reçues
        if (!allSectionsReceived()) {
            LOGGER.debug("Toutes les sections ne sont pas encore reçues. Sections reçues : {}", sectionsReceived);
            return;
        }

        if (homeWrapper == null) {
            homeWrapper = new VBox(15.0);
        } else {
            homeWrapper.getChildren().clear();
        }


        // Afficher dans l'ordre fixe souhaité, en ignorant les sections nulles
        if (queueBox != null) homeWrapper.getChildren().add(queueBox);
        if (inboxBox != null) homeWrapper.getChildren().add(inboxBox);
        if (classicBox != null) homeWrapper.getChildren().add(classicBox);
        if (surpriseBox != null) homeWrapper.getChildren().add(surpriseBox);
        if (downloadBox != null) homeWrapper.getChildren().add(downloadBox);


        if (!container.getChildren().contains(homeWrapper)) {
            container.getChildren().add(homeWrapper);
        }

        LOGGER.debug("Page d'accueil mise à jour avec {} sections", container.getChildren().size());
    }

    public void updateSection(List<? extends Item> items, String title, String sectionStr) {
        Section section;
        try {
            section = Section.valueOf(sectionStr);
        } catch (IllegalArgumentException _) {
            LOGGER.debug("Section inconnue ignorée : {}", sectionStr);
            return;
        }

        sectionsReceived.add(section);

        switch (section) {
            case INBOX -> {
                inboxBox = buildSectionBox();
                inboxBox.getChildren().addAll(getTitleSection(title), buildEpisodeList(items));
            }
            case DOWNLOAD -> {
                downloadBox = buildSectionBox();
                downloadBox.getChildren().addAll(getTitleSection(title), buildEpisodeList(items));
            }
            case QUEUE -> {
                queueBox = buildSectionBox();
                queueBox.getChildren().addAll(getTitleSection(title), buildScrollPane(buildEpisodeCardsRow(items)));
            }
            case PODCAST -> {
                classicBox = buildSectionBox();
                classicBox.getChildren().addAll(getTitleSection(title), buildScrollPane(buildImageCardsRow(items)));
            }

            case SURPRISE -> {
                surpriseBox = buildSectionBox();
                GridPane box = new GridPane();
                box.setVgap(15.0);
                box.setHgap(15.0);
                box.setPrefWidth(Region.USE_PREF_SIZE);
                box.setMaxHeight(Region.USE_PREF_SIZE);
                updateSurprise(items, box);
                surpriseBox.getChildren().addAll(getTitleSection(title), box);

            }
            default -> LOGGER.debug("Section null");
        }

        // Automatiquement mettre à jour la page d'accueil si toutes les sections sont reçues
        if (allSectionsReceived()) {
            updateHomePage();
        }
    }

    private VBox buildSectionBox() {
        VBox box = new VBox(12);
        box.setPrefWidth(Region.USE_PREF_SIZE);
        box.setMaxHeight(Region.USE_PREF_SIZE);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private ScrollPane buildScrollPane(HBox content) {
        content.setPadding(HSCROLL_PADDING);
        content.setBackground(GREY_BG);
        HBox.setHgrow(content, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(GREY_BG);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        return scrollPane;
    }

    private boolean allSectionsReceived() {
        return sectionsReceived.containsAll(REQUIRED_SECTIONS);
    }


    private Label getTitleSection(String title) {
        Label label = new Label(title);
        label.setFont(SECTION_TITLE_FONT);
        label.setTextFill(SECTION_TITLE_COLOR);
        return label;
    }


    private void initializeGrid() {
        gridInitializer.run();
    }

    private void initializeGridIfNeeded() {
        if (grid.getChildren().isEmpty() && grid.getVgap() == 0) {
            initializeGrid();
        }
    }

    private VBox buildEpisodeList(List<? extends Item> items) {
        VBox box = new VBox();
        List<Node> nodes = new ArrayList<>(items.size());
        for (Item item : items) {
            nodes.add(EPISODE_COMPONENT.createEpisode((EpisodeItem) item));
        }
        box.getChildren().addAll(nodes);
        return box;
    }

    private HBox buildImageCardsRow(List<? extends Item> items) {
        HBox row = new HBox(15.0);
        List<Node> cards = new ArrayList<>(items.size());
        for (Item item : items) {
            EpisodeItem ep = (EpisodeItem) item;
            cards.add(Cards.image(ep.getUrlImage()));
        }
        row.getChildren().addAll(cards);
        return row;
    }

    private HBox buildEpisodeCardsRow(List<? extends Item> items) {
        HBox row = new HBox(15.0);
        List<Node> cards = new ArrayList<>(items.size());
        for (Item item : items) {
            EpisodeItem ep = (EpisodeItem) item;
            cards.add(Cards.image(ep.getUrlImage(), ep.getTitle(), ep.getDate()));
        }
        row.getChildren().addAll(cards);
        return row;
    }


    private enum Section {QUEUE, INBOX, PODCAST, DOWNLOAD, SURPRISE}
}
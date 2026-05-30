package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.item.*;
import fr.github.ethanpod.event.updated.PodcastByIdUpdated;
import fr.github.ethanpod.view.component.EpisodeComponent;
import fr.github.ethanpod.view.component.SurpriseComponent;
import fr.github.ethanpod.view.component.image.ImageComponent;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PageContentRenderer {
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private static final ImageComponent IMAGE_COMPONENT = new ImageComponent();
    private static final SurpriseComponent SURPRISE_COMPONENT = new SurpriseComponent();
    private static final Logger LOGGER = LogManager.getLogger(PageContentRenderer.class);
    private final VBox container;
    private final FlowPane grid;
    private final Runnable gridInitializer;
    // Tracking des sections reçues
    private final Set<String> sectionsReceived = new HashSet<>();
    private final Set<String> expectedSections = Set.of("QUEUE", "INBOX", "PODCAST", "DOWNLOAD");
    // Variables pour les sections dans l'ordre fixe souhaité
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

    private HBox addHImageItems(List<? extends Item> episodeItems) {
        HBox classicContainer = new HBox(15.0);
        classicContainer.getChildren().clear();
        for (Item item : episodeItems) {
            EpisodeItem episodeItem = (EpisodeItem) item;
            classicContainer.getChildren().add(IMAGE_COMPONENT.createImageCard(episodeItem.getUrlImage()));
        }
        return classicContainer;
    }

    private HBox addHEpisodeItem(List<? extends Item> episodeItems) {
        HBox topQueue = new HBox(15.0);
        topQueue.getChildren().clear();
        for (Item item : episodeItems) {
            EpisodeItem episodeItem = (EpisodeItem) item;
            topQueue.getChildren().add(IMAGE_COMPONENT.createImageCard(episodeItem.getUrlImage(), episodeItem.getTitle(), episodeItem.getDate()));
        }
        return topQueue;
    }

    private VBox addVEpisodeItems(List<? extends Item> episodeItems) {
        VBox box = new VBox();
        box.getChildren().clear();
        for (Item episodeItem : episodeItems) {
            box.getChildren().add(EPISODE_COMPONENT.createEpisode((EpisodeItem) episodeItem));
        }
        return box;
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

        episodes.forEach(episode ->
                container.getChildren().add(EPISODE_COMPONENT.createEpisode((EpisodeItem) episode))
        );
    }

    public void updateSubscriptions(List<? extends Item> items, boolean append) {
        if (!append) {
            initializeGrid();
            grid.getChildren().clear();
            container.getChildren().clear();
        } else {
            initializeGridIfNeeded();
        }

        for (Item item : items) {
            NavigationItem navigationItem = (NavigationItem) item;
            grid.getChildren().add(IMAGE_COMPONENT.createImageCard(
                    navigationItem.getUrlImage(), navigationItem.getTitle(), navigationItem.getNumber()));
        }

        if (!append && !container.getChildren().contains(grid)) {
            container.getChildren().add(grid);
        }
    }

    public void updateSurprise(List<? extends Item> items, GridPane box) {
        int numColumns = 3;

        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setPercentWidth(100.0 / numColumns);
            box.getColumnConstraints().add(column);
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

        container.getChildren().clear();
        VBox box = new VBox(15.0);

        // Afficher dans l'ordre fixe souhaité, en ignorant les sections nulles
        if (queueBox != null) {
            box.getChildren().add(queueBox);
        }
        if (inboxBox != null) {
            box.getChildren().add(inboxBox);
        }
        if (classicBox != null) {
            box.getChildren().add(classicBox);
        }
        if (surpriseBox != null) {
            box.getChildren().add(surpriseBox);
        }
        if (downloadBox != null) {
            box.getChildren().add(downloadBox);
        }

        container.getChildren().add(box);

        LOGGER.debug("Page d'accueil mise à jour avec {} sections", container.getChildren().size());
    }

    private ScrollPane getScollPane(HBox box) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        box.setPadding(new Insets(0.0, 1.0, 0.0, 1.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(box, Priority.ALWAYS);
        scrollPane.setContent(box);
        return scrollPane;
    }

    public void updateSection(List<? extends Item> items, String title, String section) {
        sectionsReceived.add(section);
        switch (section) {
            case "INBOX" -> {
                inboxBox = new VBox(12);
                setPropertiesBox(inboxBox);
                VBox inboxContainer = addVEpisodeItems(items);
                inboxBox.getChildren().add(getTitleSection(title));
                inboxBox.getChildren().add(inboxContainer);
            }
            case "DOWNLOAD" -> {
                downloadBox = new VBox(12);
                setPropertiesBox(downloadBox);
                VBox topDownload = addVEpisodeItems(items);
                downloadBox.getChildren().add(getTitleSection(title));
                downloadBox.getChildren().add(topDownload);
            }
            case "QUEUE" -> {
                queueBox = new VBox(12);
                setPropertiesBox(queueBox);
                HBox topQueue = addHEpisodeItem(items);
                queueBox.getChildren().add(getTitleSection(title));
                queueBox.getChildren().add(getScollPane(topQueue));
            }
            case "PODCAST" -> {
                classicBox = new VBox(12);
                setPropertiesBox(classicBox);
                HBox classicContainer = addHImageItems(items);
                classicBox.getChildren().add(getTitleSection(title));
                classicBox.getChildren().add(getScollPane(classicContainer));
            }

            case "SURPRISE" -> {
                surpriseBox = new VBox(12);
                setPropertiesBox(surpriseBox);
                GridPane box = new GridPane();
                box.setVgap(15.0);
                box.setHgap(15.0);
                box.setPrefWidth(Region.USE_PREF_SIZE);
                box.setMaxHeight(Region.USE_PREF_SIZE);
                surpriseBox.getChildren().add(getTitleSection(title));
                surpriseBox.getChildren().add(box);
                updateSurprise(items, box);
            }
            default -> LOGGER.debug("Section null");
        }

        // Automatiquement mettre à jour la page d'accueil si toutes les sections sont reçues
        if (allSectionsReceived()) {
            updateHomePage();
        }
    }

    private void setPropertiesBox(VBox inboxBox) {
        inboxBox.setPrefWidth(Region.USE_PREF_SIZE);
        inboxBox.setMaxHeight(Region.USE_PREF_SIZE);
        HBox.setHgrow(inboxBox, Priority.ALWAYS);
    }

    private boolean allSectionsReceived() {
        return sectionsReceived.containsAll(expectedSections);
    }

    private Label getTitleSection(String title) {
        Label label = new Label(title);
        label.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        label.setTextFill(ColorThemeConstants.getMain950());
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
}
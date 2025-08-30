package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.event.PodcastFindByIdUpdate;
import fr.github.ethanpod.view.component.EpisodeComponent;
import fr.github.ethanpod.view.component.image.ImageComponent;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.geometry.Insets;
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

    public PageContentRenderer(VBox container, FlowPane grid, Runnable gridInitializer) {
        this.container = container;
        this.grid = grid;
        this.gridInitializer = gridInitializer;
    }

    public void updatePodcastTitle(PodcastFindByIdUpdate event) {
        LOGGER.info("Getting podcast: {}", event.getPodcastItem().getTitle());

        VBox subtitle = new VBox();
        subtitle.getChildren().addAll(
                new Label(event.getPodcastItem().getAuthor()),
                new Label(event.getPodcastItem().getDescription())
        );
        container.getChildren().add(subtitle);
    }

    public void updateEpisodes(List<EpisodeItem> episodes, boolean append) {
        LOGGER.info("Getting {} episodes, append: {}", episodes.size(), append);

        if (!append) {
            container.getChildren().removeIf(node ->
                    node instanceof VBox vbox &&
                            (vbox.getChildren().isEmpty() || !(vbox.getChildren().getFirst() instanceof Label))
            );
        }

        episodes.forEach(episode ->
                container.getChildren().add(EPISODE_COMPONENT.createEpisode(episode))
        );
    }

    public void updateSubscriptions(List<NavigationItem> items, boolean append) {
        if (!append) {
            initializeGrid();
            grid.getChildren().clear();
            container.getChildren().clear();
        } else {
            initializeGridIfNeeded();
        }

        items.forEach(item ->
                grid.getChildren().add(IMAGE_COMPONENT.createImageCard(
                        item.getUrlImage(), item.getTitle(), item.getNumber()))
        );

        if (!append && !container.getChildren().contains(grid)) {
            container.getChildren().add(grid);
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

    public void updateSection(List<EpisodeItem> episodeItems, String title, String section) {
        sectionsReceived.add(section);

        switch (section) {
            case "INBOX" -> {
                inboxBox = new VBox(12);
                inboxBox.setPrefWidth(Region.USE_PREF_SIZE);
                inboxBox.setMaxHeight(Region.USE_PREF_SIZE);
                HBox.setHgrow(inboxBox, Priority.ALWAYS);
                inboxBox.getChildren().add(getTitleSection(title));
                VBox inboxContainer = new VBox();
                inboxBox.getChildren().add(inboxContainer);
                inboxContainer.getChildren().clear();
                for (EpisodeItem episodeItem : episodeItems) {
                    inboxContainer.getChildren().add(EPISODE_COMPONENT.createEpisode(episodeItem));
                }
            }
            case "DOWNLOAD" -> {
                downloadBox = new VBox(12);
                downloadBox.setPrefWidth(Region.USE_PREF_SIZE);
                downloadBox.setMaxHeight(Region.USE_PREF_SIZE);
                HBox.setHgrow(downloadBox, Priority.ALWAYS);
                downloadBox.getChildren().add(getTitleSection(title));
                VBox topDownload = new VBox();
                downloadBox.getChildren().add(topDownload);
                topDownload.getChildren().clear();
                for (EpisodeItem episodeItem : episodeItems) {
                    topDownload.getChildren().add(EPISODE_COMPONENT.createEpisode(episodeItem));
                }
            }
            case "QUEUE" -> {
                queueBox = new VBox(12);
                queueBox.setPrefWidth(Region.USE_PREF_SIZE);
                queueBox.setMaxHeight(Region.USE_PREF_SIZE);
                HBox.setHgrow(queueBox, Priority.ALWAYS);
                queueBox.getChildren().add(getTitleSection(title));
                HBox topQueue = new HBox(15.0);
                queueBox.getChildren().add(getScollPane(topQueue));
                topQueue.getChildren().clear();
                for (EpisodeItem episodeItem : episodeItems) {
                    topQueue.getChildren().add(IMAGE_COMPONENT.createImageCard(episodeItem.getUrlImage(), episodeItem.getTitle(), episodeItem.getDate()));
                }
            }
            case "PODCAST" -> {
                classicBox = new VBox(12);
                classicBox.setPrefWidth(Region.USE_PREF_SIZE);
                classicBox.setMaxHeight(Region.USE_PREF_SIZE);
                HBox.setHgrow(classicBox, Priority.ALWAYS);
                classicBox.getChildren().add(getTitleSection(title));
                HBox classicContainer = new HBox(15.0);
                classicBox.getChildren().add(getScollPane(classicContainer));
                classicContainer.getChildren().clear();
                for (EpisodeItem episodeItem : episodeItems) {
                    classicContainer.getChildren().add(IMAGE_COMPONENT.createImageCard(episodeItem.getUrlImage()));
                }
            }
            default -> LOGGER.debug("Section null");
        }

        // Automatiquement mettre à jour la page d'accueil si toutes les sections sont reçues
        if (allSectionsReceived()) {
            updateHomePage();
        }
    }

    /**
     * Vérifie si toutes les sections attendues ont été reçues
     */
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
package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.event.*;
import fr.github.ethanpod.view.component.episode.EpisodeComponent;
import fr.github.ethanpod.view.component.image.ImageComponent;
import fr.github.ethanpod.view.component.surprise.SurpriseComponent;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.HomeContext;
import fr.github.ethanpod.view.context.LayoutContext;
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

import java.util.List;

public class HomeLayout extends Layout implements ContextualLayout {
    private static final String TITLE_EXAMPLE = "Lil Nas X, une icône noire, et gay et flamboyante [REDIF]";
    private static final String IMAGE_HDM = String.valueOf(HomeLayout.class.getResource("/images/heure_du_monde.png"));
    private static final SurpriseComponent SURPRISE_COMPONENT = new SurpriseComponent();
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private static final ImageComponent IMAGE_COMPONENT = new ImageComponent();
    private static final Logger log = LogManager.getLogger(HomeLayout.class);
    private HBox topQueue;
    private VBox topDownload;
    private HBox classicContainer;
    private VBox inboxContainer;
    private VBox mainContainer;
    private Label newLabel;

    public HomeLayout(UIEventManager uiEventManager) {
        super("Home", uiEventManager);
        registerEventHandlers();
        MessageRouter.getInstance().userRequest(UserRequestType.GET_INBOX_TOP8, "[INBOX]", null);
        MessageRouter.getInstance().userRequest(UserRequestType.GET_DOWNLOAD_TOP8, "[DOWNLOAD]", null);
        MessageRouter.getInstance().userRequest(UserRequestType.GET_QUEUE_TOP8, "[QUEUE", null);
        MessageRouter.getInstance().userRequest(UserRequestType.GET_PODCAST_READ_TOP8, "[PODCAST]", null);
    }

    private VBox getNewsSection() {
        VBox box = getMainBox();
        newLabel = getTitleSection("See what's news");
        box.getChildren().add(newLabel);
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
        box.getChildren().add(getDownload());
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
        inboxContainer = new VBox();
        return inboxContainer;

    }

    private ScrollPane getClassic() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        classicContainer = new HBox(15);
        classicContainer.setPadding(new Insets(0.0, 1.0, 0.0, 1.0));
        classicContainer.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(classicContainer, Priority.ALWAYS);
        scrollPane.setContent(classicContainer);
        return scrollPane;
    }

    private ScrollPane getListening() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        topQueue = new HBox(15);
        topQueue.setPadding(new Insets(0.0, 1.0, 0.0, 1.0));
        topQueue.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(topQueue, Priority.ALWAYS);
        scrollPane.setContent(topQueue);
        return scrollPane;
    }

    private VBox getDownload() {
        topDownload = new VBox();
        return topDownload;
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

    private void registerEventHandlers() {
        // Créer des handlers spécifiques pour chaque type d'événement
        UIEventHandler<QueueTop8UpdateEvent> queueTop8UpdateEventUIEventHandler = event -> {
            log.info("Mise à jour de la queue avec {} éléments", event.getEpisodeItems().size());
            updateTopQueue(event.getEpisodeItems());
        };

        UIEventHandler<InboxTop8UpdatedEvent> inboxTop8UpdatedEventUIEventHandler = event -> {
            log.info("Mise à jour d'inbox container avec {} éléments", event.getEpisodeItems().size());
            updateTopInbox(event.getEpisodeItems());
        };

        UIEventHandler<PodcastTop8UpdateEvent> podcastTop8UpdateEventUIEventHandler = event -> {
            log.info("Mise à jour de la classique avec {} éléments", event.getEpisodeItems().size());
            updateTopPodcast(event.getEpisodeItems());
        };

        UIEventHandler<InboxCountUpdatedEvent> inboxCountUpdatedEventUIEventHandler = event -> {
            log.info("Inbox count update: {}", event.getCount());
            String tet = newLabel.getText();
            newLabel.setText(tet + " (" + event.getCount() + ")");
        };

        UIEventHandler<DownloadTop8UpdatedEvent> downloadTop8UpdatedEventUIEventHandler = event -> {
            log.info("Mise à jour download avec {} éléments", event.getEpisodeItems().size());
            updateTopDownload(event.getEpisodeItems());
        };
        // Enregistrement des handlers
        eventManager.registerHandler(EventType.QUEUE_TOP8_UPDATED, queueTop8UpdateEventUIEventHandler);
        eventManager.registerHandler(EventType.INBOX_TOP8_UPDATED, inboxTop8UpdatedEventUIEventHandler);
        eventManager.registerHandler(EventType.PODCAST_TOP8_UPDATED, podcastTop8UpdateEventUIEventHandler);
        eventManager.registerHandler(EventType.INBOX_COUNT_UPDATED, inboxCountUpdatedEventUIEventHandler);
        eventManager.registerHandler(EventType.DOWNLOAD_TOP8_UPDATED, downloadTop8UpdatedEventUIEventHandler);
    }

    private void updateTopDownload(List<EpisodeItem> episodeItems) {
        topDownload.getChildren().clear();
        for (EpisodeItem episodeItem : episodeItems) {
            topDownload.getChildren().add(EPISODE_COMPONENT.createInboxEpisode(episodeItem));
        }
    }

    private void updateTopQueue(List<EpisodeItem> episodeItems) {
        topQueue.getChildren().clear();
        for (EpisodeItem episodeItem : episodeItems) {
            topQueue.getChildren().add(IMAGE_COMPONENT.createImageCard(episodeItem.getUrlImage(), episodeItem.getName(), episodeItem.getDate()));
        }
    }

    private void updateTopInbox(List<EpisodeItem> episodeItems) {
        inboxContainer.getChildren().clear();
        for (EpisodeItem episodeItem : episodeItems) {
            inboxContainer.getChildren().add(EPISODE_COMPONENT.createInboxEpisode(episodeItem));
        }
    }

    private void updateTopPodcast(List<EpisodeItem> episodeItems) {
        classicContainer.getChildren().clear();
        for (EpisodeItem episodeItem : episodeItems) {
            classicContainer.getChildren().add(IMAGE_COMPONENT.createImageCard(episodeItem.getUrlImage()));
        }
    }

    @Override
    public void updateContext(LayoutContext context) {
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return HomeContext.class.isAssignableFrom(contextType);
    }
}

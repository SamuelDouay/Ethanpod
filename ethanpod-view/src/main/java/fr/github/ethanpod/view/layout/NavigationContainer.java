package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.core.item.ItemManager;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.view.component.navigation.NavigationComponent;
import fr.github.ethanpod.view.context.FeedContext;
import fr.github.ethanpod.view.event.InboxCountUpdatedEvent;
import fr.github.ethanpod.view.event.NavigationUpdatedEvent;
import fr.github.ethanpod.view.event.UIEventHandler;
import fr.github.ethanpod.view.event.UIEventManager;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;

import java.util.ArrayList;
import java.util.List;

public class NavigationContainer {
    private static final String FONT = "Inter";
    private static final Logger log = LogManager.getLogger(NavigationContainer.class);
    private final ItemManager manager;
    private final List<HBox> listNav;
    private final UIEventManager eventManager;
    private final LayoutManager layoutManager;
    private VBox scrollBox;

    public NavigationContainer(LayoutManager layoutManager, UIEventManager uiEventManager) {
        this.layoutManager = layoutManager;
        this.eventManager = uiEventManager;
        this.manager = new ItemManager();
        this.listNav = new ArrayList<>();
        registerEventHandlers();
    }

    public VBox createMenu() {
        VBox mainContainer = new VBox();
        mainContainer.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain000(), null, null)));
        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        mainContainer.getChildren().addAll(createFixedList(), createScrollList());
        return mainContainer;
    }

    private VBox createList() {
        VBox box = new VBox();
        box.setPadding(new Insets(8.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain000(), null, null)));
        box.setMinWidth(Region.USE_PREF_SIZE);
        return box;
    }

    private VBox createFixedList() {
        VBox box = createList();

        NavigationItem homeItem = new NavigationItem(MaterialDesignH.HOME.getDescription(), "Home", true);
        homeItem.setSelected(true);
        NavigationItem playlistItem = new NavigationItem(MaterialDesignP.PLAYLIST_PLAY.getDescription(), "Queue", true);
        NavigationItem inboxItem = new NavigationItem(MaterialDesignI.INBOX.getDescription(), "Inbox", 120, true);
        NavigationItem episodesItem = new NavigationItem(MaterialDesignR.RSS.getDescription(), "Episodes", true);
        NavigationItem subscriptionsItem = new NavigationItem(MaterialDesignV.VIEW_GRID_OUTLINE.getDescription(), "Subscription", 120, true);
        NavigationItem downloadsItem = new NavigationItem(MaterialDesignD.DOWNLOAD.getDescription(), "Downloads", 123, true);
        NavigationItem historyItem = new NavigationItem(MaterialDesignH.HISTORY.getDescription(), "Playback history", true);
        NavigationItem addPodcastItem = new NavigationItem(MaterialDesignP.PLUS.getDescription(), "Add podcast", true);

        listNav.add(createNavigationComponent(homeItem, LayoutType.HOME));
        listNav.add(createNavigationComponent(playlistItem, LayoutType.QUEUE));
        listNav.add(createNavigationComponent(inboxItem, LayoutType.INBOX));
        listNav.add(createNavigationComponent(episodesItem, LayoutType.EPISODES));
        listNav.add(createNavigationComponent(subscriptionsItem, LayoutType.SUBSCRIPTION));
        listNav.add(createNavigationComponent(downloadsItem, LayoutType.DOWNLOAD));
        listNav.add(createNavigationComponent(historyItem, LayoutType.HISTORY));
        listNav.add(createNavigationComponent(addPodcastItem, LayoutType.ADD));

        box.getChildren().addAll(listNav);

        return box;
    }

    private ScrollPane createScrollList() {
        scrollBox = createList();
        ScrollPane scrollPane = getScrollPane(scrollBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return scrollPane;
    }

    private ScrollPane getScrollPane(VBox box) {
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain000(), null, null)));
        scrollPane.setBorder(new Border(new BorderStroke(ColorThemeConstants.getGrey950(), BorderStrokeStyle.SOLID, null, new BorderWidths(1.0, 0, 0, 0))));
        return scrollPane;
    }

    private HBox createNavigationComponent(NavigationItem item, LayoutType layoutType) {
        manager.addItem(item);
        NavigationComponent container = new NavigationComponent();
        HBox box = container.createNavigationCard(item);

        box.setOnMouseClicked(_ -> {
            manager.setItemState(true, item.getUuid());
            for (HBox hBox : listNav) {
                updateAppearance(hBox, hBox.equals(box));
            }
            if (layoutType != null && layoutManager != null) {
                if (layoutType.equals(LayoutType.FEED)) {
                    FeedContext context = new FeedContext(item.getTitle(), item.getUuid().toString(), item.getNumber());
                    layoutManager.setLayout(layoutType, context);

                } else {
                    layoutManager.setLayout(layoutType);
                }
            }
        });
        box.setOnMouseEntered(_ -> updateAppearance(box, true));
        box.setOnMouseExited(_ -> updateAppearance(box, item.isSelected()));
        box.setOnMousePressed(_ -> box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain050(), null, null))));
        box.setOnMouseReleased(_ -> updateAppearance(box, item.isSelected()));

        return box;
    }

    private void updateAppearance(HBox mainBox, boolean isSelected) {
        Label titleLabel = (Label) ((HBox) mainBox.getChildren().getFirst()).getChildren().get(1);
        Node icon = ((HBox) mainBox.getChildren().getFirst()).getChildren().get(0);

        Background background = new Background(new BackgroundFill(
                isSelected ? ColorThemeConstants.getMain100() : Color.TRANSPARENT,
                new CornerRadii(2.0),
                null
        ));
        updateBackgroundIfNeeded(mainBox, background);

        if (isSelected) {
            titleLabel.setTextFill(ColorThemeConstants.getMain950());
            titleLabel.setFont(Font.font(FONT, FontWeight.BOLD, 12));
            if (!(icon instanceof FontIcon)) {
                return;
            }
            ((FontIcon) icon).setIconColor(ColorThemeConstants.getMain950());
        } else {
            titleLabel.setTextFill(ColorThemeConstants.getGrey800());
            titleLabel.setFont(Font.font(FONT, FontPosture.REGULAR, 12));
            if (!(icon instanceof FontIcon)) {
                return;
            }
            ((FontIcon) icon).setIconColor(ColorThemeConstants.getGrey800());
        }
    }


    private void updateNavigationList(List<NavigationItem> navigationList) {
        scrollBox.getChildren().clear();
        for (NavigationItem navigationItem : navigationList) {
            try {
                Node component = createNavigationComponent(navigationItem, LayoutType.FEED);
                scrollBox.getChildren().add(component);
            } catch (Exception e) {
                log.error("Erreur création composant pour {}", navigationItem, e);
            }
        }
    }

    private void updateBackgroundIfNeeded(Region region, Background newBackground) {
        Background current = region.getBackground();
        if (current == null || !current.equals(newBackground)) {
            region.setBackground(newBackground);
        }
    }

    private void updateInboxCount(Integer count) {
        log.info(count);
    }

    private void registerEventHandlers() {
        // Créer des handlers spécifiques pour chaque type d'événement
        UIEventHandler<NavigationUpdatedEvent> navigationHandler = event -> {
            log.info("Mise à jour de la navigation avec {} éléments", event.getItemCount());
            updateNavigationList(event.getNavigationItems());
        };

        UIEventHandler<InboxCountUpdatedEvent> inboxHandler = event -> {
            log.info("Mise à jour du compteur inbox: {}",
                    event.getCount());
            updateInboxCount(event.getCount());
        };

        // Enregistrement des handlers
        eventManager.registerHandler(EventType.NAVIGATION_UPDATED, navigationHandler);
        eventManager.registerHandler(EventType.INBOX_COUNT_UPDATED, inboxHandler);
    }
}
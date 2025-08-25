package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.item.ItemManager;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.event.InboxCountUpdatedEvent;
import fr.github.ethanpod.event.NavigationUpdatedEvent;
import fr.github.ethanpod.event.UIEventHandler;
import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.component.NavigationComponent;
import fr.github.ethanpod.view.context.PageContext;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.materialdesign2.*;

import java.util.List;

public class NavigationContainer {
    private static final Logger log = LogManager.getLogger(NavigationContainer.class);

    // Réutilisation d'objets pour éviter les allocations
    private static final Background MAIN_BG = new Background(
            new BackgroundFill(ColorThemeConstants.getMain000(), null, null)
    );
    private static final Background PRESSED_BG = new Background(
            new BackgroundFill(ColorThemeConstants.getMain050(), null, null)
    );
    private static final Border SCROLL_BORDER = new Border(
            new BorderStroke(ColorThemeConstants.getGrey950(), BorderStrokeStyle.SOLID, null, new BorderWidths(1.0, 0, 0, 0))
    );
    private static final Insets LIST_PADDING = new Insets(8.0);

    private final ItemManager manager = new ItemManager();
    private final LayoutManager layoutManager;
    private final NavigationComponent navComponent = new NavigationComponent();

    // Arrays fixes pour éviter les listes dynamiques
    private final HBox[] fixedNavBoxes = new HBox[8];
    private final NavigationItem[] fixedItems = new NavigationItem[8];
    private Label inboxBadgeLabel;
    private VBox scrollBox;
    private HBox selectedBox;

    public NavigationContainer(LayoutManager layoutManager, UIEventManager uiEventManager) {
        this.layoutManager = layoutManager;
        registerEventHandlers(uiEventManager);
        createFixedItems();
        MessageRouter.getInstance().userRequest(UserRequestType.GET_NAVIGATION_LIST, "[NAVIGATION]", null);
        MessageRouter.getInstance().userRequest(UserRequestType.GET_INBOX_COUNT, "[INBOX]", null);
    }

    public VBox createMenu() {
        VBox mainContainer = new VBox(createFixedList(), createScrollList());
        mainContainer.setBackground(MAIN_BG);
        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        return mainContainer;
    }

    private void createFixedItems() {
        fixedItems[0] = new NavigationItem(MaterialDesignH.HOME.getDescription(), "Home", true);
        fixedItems[0].setSelected(true);
        fixedItems[1] = new NavigationItem(MaterialDesignP.PLAYLIST_PLAY.getDescription(), "Queue", true);
        fixedItems[2] = new NavigationItem(MaterialDesignI.INBOX.getDescription(), "Inbox", true);
        fixedItems[3] = new NavigationItem(MaterialDesignR.RSS.getDescription(), "Episodes", true);
        fixedItems[4] = new NavigationItem(MaterialDesignV.VIEW_GRID_OUTLINE.getDescription(), "Subscriptions", true);
        fixedItems[5] = new NavigationItem(MaterialDesignD.DOWNLOAD.getDescription(), "Downloads", true);
        fixedItems[6] = new NavigationItem(MaterialDesignH.HISTORY.getDescription(), "Playback history", true);
        fixedItems[7] = new NavigationItem(MaterialDesignP.PLUS.getDescription(), "Add podcast", true);

        for (NavigationItem item : fixedItems) {
            manager.addItem(item);
        }
    }

    private VBox createFixedList() {
        VBox box = createListContainer();

        for (int i = 0; i < fixedItems.length; i++) {
            fixedNavBoxes[i] = createOptimizedNavigationBox(fixedItems[i], LayoutType.PAGE);
            if (fixedItems[i].isSelected()) {
                selectedBox = fixedNavBoxes[i];
            }

            if (i == 2 && fixedItems[i].getNumber() > 0) {
                inboxBadgeLabel = findBadgeLabel(fixedNavBoxes[i]);
            }
        }

        box.getChildren().addAll(fixedNavBoxes);
        return box;
    }

    private ScrollPane createScrollList() {
        scrollBox = createListContainer();
        ScrollPane scrollPane = createOptimizedScrollPane(scrollBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return scrollPane;
    }

    private VBox createListContainer() {
        VBox box = new VBox();
        box.setPadding(LIST_PADDING);
        box.setBackground(MAIN_BG);
        box.setMinWidth(Region.USE_PREF_SIZE);
        return box;
    }

    private ScrollPane createOptimizedScrollPane(VBox box) {
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(MAIN_BG);
        scrollPane.setBorder(SCROLL_BORDER);
        return scrollPane;
    }

    private HBox createOptimizedNavigationBox(NavigationItem item, LayoutType layoutType) {
        HBox box = navComponent.createNavigationCard(item);


        box.setOnMouseClicked(_ -> handleNavClick(box, item, layoutType));
        box.setOnMouseEntered(_ -> NavigationComponent.updateAppearance(box, true, true));
        box.setOnMouseExited(_ -> NavigationComponent.updateAppearance(box, item.isSelected(), false));
        box.setOnMousePressed(_ -> box.setBackground(PRESSED_BG));
        box.setOnMouseReleased(_ -> NavigationComponent.updateAppearance(box, item.isSelected(), false));

        return box;
    }

    private void handleNavClick(HBox clickedBox, NavigationItem item, LayoutType layoutType) {
        manager.setItemState(true, item.getUuid());


        if (selectedBox != null && selectedBox != clickedBox) {
            NavigationComponent.updateAppearance(selectedBox, false, false);
        }
        selectedBox = clickedBox;
        NavigationComponent.updateAppearance(selectedBox, true, false);


        if (layoutType != null && layoutManager != null) {
            PageContext context = new PageContext(item.getTitle(), item.getId());
            layoutManager.setLayout(layoutType, context);
        }
    }

    private void updateNavigationList(List<NavigationItem> navigationList) {
        scrollBox.getChildren().clear();

        for (NavigationItem item : navigationList) {
            try {
                HBox component = createOptimizedNavigationBox(item, LayoutType.PAGE);
                scrollBox.getChildren().add(component);
            } catch (Exception e) {
                log.error("Erreur création composant pour {}", item, e);
            }
        }
    }

    private void updateInboxCount(Integer count) {
        if (count != null && count > 0) {
            fixedItems[2].setNumber(count);

            // Mise à jour directe du badge existant
            if (inboxBadgeLabel != null) {
                inboxBadgeLabel.setText(String.valueOf(count));
            } else {
                // Création du badge si il n'existait pas
                HBox oldBox = fixedNavBoxes[2];
                HBox newBox = createOptimizedNavigationBox(fixedItems[2], LayoutType.PAGE);

                VBox parent = (VBox) oldBox.getParent();
                int index = parent.getChildren().indexOf(oldBox);
                parent.getChildren().set(index, newBox);

                fixedNavBoxes[2] = newBox;
                inboxBadgeLabel = findBadgeLabel(newBox);

                if (selectedBox == oldBox) {
                    selectedBox = newBox;
                }

                // Nettoyage explicite de l'ancien composant
                oldBox.setOnMouseClicked(null);
                oldBox.setOnMouseEntered(null);
                oldBox.setOnMouseExited(null);
                oldBox.setOnMousePressed(null);
                oldBox.setOnMouseReleased(null);
            }

            log.debug("Inbox count updated: {}", count);
        }
    }

    private Label findBadgeLabel(HBox navBox) {
        if (navBox.getChildren().size() >= 3) {
            return (Label) navBox.getChildren().get(2);
        }
        return null;
    }

    private void registerEventHandlers(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.NAVIGATION_UPDATED,
                (UIEventHandler<NavigationUpdatedEvent>) event -> {
                    log.info("Navigation update: {} items", event.getItemCount());
                    updateNavigationList(event.getNavigationItems());
                }
        );

        eventManager.registerHandler(EventType.INBOX_COUNT_UPDATED,
                (UIEventHandler<InboxCountUpdatedEvent>) event -> {
                    log.info("Inbox count update: {}", event.getCount());
                    updateInboxCount(event.getCount());
                }
        );
    }
}
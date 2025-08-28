package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.event.PodcastFindByIdUpdate;
import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.component.EpisodeComponent;
import fr.github.ethanpod.view.component.image.ImageComponent;
import fr.github.ethanpod.view.context.CleanableLayout;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.LayoutContext;
import fr.github.ethanpod.view.context.PageContext;
import fr.github.ethanpod.view.util.ImageCache;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PageLayout extends Layout implements ContextualLayout, CleanableLayout {

    // Constants
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private static final ImageComponent IMAGE_COMPONENT = new ImageComponent();
    private static final String DEFAULT_TITLE = "PAGE";
    private static final Logger LOGGER = LogManager.getLogger(PageLayout.class);

    // Pagination configuration
    private static final int PAGE_SIZE = 100;
    private static final double SCROLL_THRESHOLD = 0.9;
    private static final int INITIAL_PAGE = 0;

    // Page types
    private static final String QUEUE_PAGE = "Queue";
    private static final String INBOX_PAGE = "Inbox";
    private static final String DOWNLOADS_PAGE = "Downloads";
    private static final String SUBSCRIPTIONS_PAGE = "Subscriptions";

    // Pagination state
    private final PaginationState paginationState = new PaginationState();
    private ChangeListener<Number> scrollListener;

    public PageLayout(UIEventManager eventManager, ScrollPane scrollPane) {
        super(DEFAULT_TITLE, eventManager, scrollPane);
        registerEventHandlers(eventManager);
    }

    @Override
    public VBox getLayout() {
        VBox mainBox = getContainer();
        mainBox.getChildren().add(getTitle());
        mainBox.getChildren().add(container);
        return mainBox;
    }

    @Override
    public void clearContainer() {
        if (container != null) {
            LOGGER.debug("Nettoyage du container PageLayout");
            container.getChildren().clear();
            ImageCache.cleanupDeadReferences();
        }
        resetPaginationState();
    }

    @Override
    public void updateContext(LayoutContext context, LayoutType layoutType) {
        clearContainer();

        if (context instanceof PageContext(String title, Integer id)) {
            initializePage(title, id);
            loadInitialData();
            setupInfiniteScroll();
        }
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return PageContext.class.isAssignableFrom(contextType);
    }

    // Private methods - Initialization
    private void initializePage(String title, Integer id) {
        setTitle(title);
        paginationState.reset(title, id);
    }

    private void resetPaginationState() {
        paginationState.reset();
        removeScrollListener();
    }

    private void removeScrollListener() {
        if (scrollListener != null) {
            scrollPane.vvalueProperty().removeListener(scrollListener);
        }
    }

    // Private methods - Data loading
    private void loadInitialData() {
        loadDataForCurrentPage();
    }

    private void loadDataForCurrentPage() {
        LOGGER.debug("Chargement page: {}", paginationState.getCurrentPage());

        if (paginationState.hasPodcastId()) {
            loadPodcastData();
        } else {
            loadPageSpecificData();
        }
    }

    private void loadPodcastData() {
        MessageRouter.getInstance().userRequest(
                UserRequestType.GET_PODCAST_BY_ID,
                "[PODCAST]",
                paginationState.getCurrentPodcastId()
        );

        MessageRouter.getInstance().userRequest(
                UserRequestType.GET_EPISODE_BY_PODCAST_ID,
                "[PODCAST]",
                createUserDataRequest()
        );
    }

    private void loadPageSpecificData() {
        String pageType = paginationState.getCurrentPageType();
        UserDataRequest request = createUserDataRequest();

        switch (pageType) {
            case QUEUE_PAGE -> sendRequest(UserRequestType.GET_QUEUE_ALL, "[QUEUE]", request);
            case INBOX_PAGE -> sendRequest(UserRequestType.GET_INBOX_ALL, "[INBOX]", request);
            case DOWNLOADS_PAGE -> sendRequest(UserRequestType.GET_DOWNLOAD_ALL, "[DOWNLOAD]", request);
            case SUBSCRIPTIONS_PAGE -> sendRequest(UserRequestType.GET_NAVIGATION_LIST, "[NAVIGATION]", request);
            default -> LOGGER.warn("Type de page non reconnu: {}", pageType);
        }
    }

    private void sendRequest(UserRequestType requestType, String tag, UserDataRequest request) {
        MessageRouter.getInstance().userRequest(requestType, tag, request);
    }

    private UserDataRequest createUserDataRequest() {
        return new UserDataRequest(
                paginationState.getCurrentPodcastId(),
                paginationState.getCurrentPage(),
                PAGE_SIZE
        );
    }

    // Private methods - Scroll handling
    private void setupInfiniteScroll() {
        scrollListener = this::handleScrollChange;
        scrollPane.vvalueProperty().addListener(scrollListener);
    }

    private void handleScrollChange(javafx.beans.value.ObservableValue<? extends Number> observable,
                                    Number oldValue, Number newValue) {
        if (shouldLoadMoreData(newValue.doubleValue())) {
            paginationState.nextPage();
            loadDataForCurrentPage();
        }
    }

    private boolean shouldLoadMoreData(double scrollValue) {
        return scrollValue >= SCROLL_THRESHOLD && paginationState.hasMoreData();
    }

    // Private methods - Event handlers registration
    private void registerEventHandlers(UIEventManager eventManager) {
        registerPodcastHandlers(eventManager);
        registerEpisodeHandlers(eventManager);
        registerNavigationHandlers(eventManager);
    }

    private void registerPodcastHandlers(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.PODCAST_BY_ID_UPDATED, this::updatePodcastTitle);
    }

    private void registerEpisodeHandlers(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.EPISODE_BY_PODCAST_ID_UPDATED,
                event -> handleEpisodeUpdate(event.getEpisodeItems()));

        eventManager.registerHandler(EventType.QUEUE_ALL_UPDATED,
                event -> handleEpisodeUpdate(event.getEpisodeItems()));

        eventManager.registerHandler(EventType.INBOX_ALL_UPDATED,
                event -> handleEpisodeUpdate(event.getEpisodeItems()));

        eventManager.registerHandler(EventType.DOWNLOAD_ALL_UPDATED,
                event -> handleEpisodeUpdate(event.getEpisodeItems()));
    }

    private void registerNavigationHandlers(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.NAVIGATION_UPDATED, event -> {
            handleNavigationUpdate(event.getNavigationItems());
        });
    }

    // Private methods - UI updates
    private void updatePodcastTitle(PodcastFindByIdUpdate event) {
        LOGGER.info("Getting podcast: {}", event.getPodcastItem().getTitle());
        setTitle(event.getPodcastItem().getTitle());
        addPodcastSubtitle(event);
    }

    private void addPodcastSubtitle(PodcastFindByIdUpdate event) {
        VBox subtitle = new VBox();
        subtitle.getChildren().add(new Label(event.getPodcastItem().getAuthor()));
        subtitle.getChildren().add(new Label(event.getPodcastItem().getDescription()));
        container.getChildren().add(subtitle);
    }

    private void handleEpisodeUpdate(List<EpisodeItem> episodes) {
        boolean isFirstPage = paginationState.isFirstPage();
        updateEpisode(episodes, !isFirstPage);
        checkForMoreData(episodes);
    }

    private void handleNavigationUpdate(List<NavigationItem> items) {
        boolean isFirstPage = paginationState.isFirstPage();
        updateSubscriptions(items, !isFirstPage);
        checkForMoreData(items);
    }

    private void updateEpisode(List<EpisodeItem> episodeItems, boolean append) {
        LOGGER.info("Getting {} episodes, append: {}", episodeItems.size(), append);

        if (!append) {
            clearContainerExceptPodcastInfo();
        }

        addEpisodesToContainer(episodeItems);
    }

    private void clearContainerExceptPodcastInfo() {
        container.getChildren().removeIf(node ->
                !(node instanceof VBox && hasLabelAsFirstChild((VBox) node))
        );
    }

    private boolean hasLabelAsFirstChild(VBox vbox) {
        return !vbox.getChildren().isEmpty() && vbox.getChildren().get(0) instanceof Label;
    }

    private void addEpisodesToContainer(List<EpisodeItem> episodeItems) {
        episodeItems.forEach(episode ->
                container.getChildren().add(EPISODE_COMPONENT.createEpisode(episode))
        );
    }

    private void updateSubscriptions(List<NavigationItem> items, boolean append) {
        if (!append) {
            initializeGridForFirstLoad();
        } else {
            ensureGridExists();
        }

        addItemsToGrid(items);
        ensureGridInContainer(append);
    }

    private void initializeGridForFirstLoad() {
        getGrid();
        grid.getChildren().clear();
        container.getChildren().clear();
    }

    private void ensureGridExists() {
        if (grid == null) {
            getGrid();
        }
    }

    private void addItemsToGrid(List<NavigationItem> items) {
        items.forEach(item ->
                grid.getChildren().add(IMAGE_COMPONENT.createImageCard(
                        item.getUrlImage(), item.getTitle(), item.getNumber()))
        );
    }

    private void ensureGridInContainer(boolean append) {
        if (!append && !container.getChildren().contains(grid)) {
            container.getChildren().add(grid);
        }
    }

    private void checkForMoreData(List<?> items) {
        if (items.isEmpty() || items.size() < PAGE_SIZE) {
            paginationState.setHasMoreData(false);
            LOGGER.debug("Plus de données disponibles - reçu {} éléments", items.size());
        }
    }

    // Inner class for pagination state management
    private static class PaginationState {
        private int currentPage = INITIAL_PAGE;
        private boolean hasMoreData = true;
        private String currentPageType = "";
        private Integer currentPodcastId = null;

        void reset() {
            reset("", null);
        }

        void reset(String pageType, Integer podcastId) {
            this.currentPage = INITIAL_PAGE;
            this.hasMoreData = true;
            this.currentPageType = pageType;
            this.currentPodcastId = podcastId;
        }

        void nextPage() {
            currentPage++;
        }

        boolean isFirstPage() {
            return currentPage == INITIAL_PAGE;
        }

        boolean hasMoreData() {
            return hasMoreData;
        }

        void setHasMoreData(boolean hasMoreData) {
            this.hasMoreData = hasMoreData;
        }

        boolean hasPodcastId() {
            return currentPodcastId != null && currentPodcastId != 0;
        }

        // Getters
        int getCurrentPage() {
            return currentPage;
        }

        String getCurrentPageType() {
            return currentPageType;
        }

        Integer getCurrentPodcastId() {
            return currentPodcastId;
        }
    }
}
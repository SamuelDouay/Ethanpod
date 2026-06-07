package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.*;
import fr.github.ethanpod.view.util.PaginationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageDataLoader {
    private static final Logger LOGGER = LogManager.getLogger(PageDataLoader.class);
    private static final int PAGE_SIZE = 100;
    private final PaginationState paginationState = new PaginationState();
    private final PageContentRenderer pageContentRenderer;

    public PageDataLoader(PageContentRenderer pageContentRenderer) {
        this.pageContentRenderer = pageContentRenderer;
    }


    public void initializeContext(String title, Integer id) {
        paginationState.reset(title, id);
    }

    public void resetPaginationState() {
        paginationState.reset();
    }

    public void loadDataForCurrentPage() {
        UserDataRequest request = createUserDataRequest();

        if (paginationState.hasPodcastId()) {
            loadPodcastData(request);
        } else {
            loadPageData(request);
        }
    }

    public void loadNextPage() {
        if (paginationState.hasMoreData()) {
            paginationState.nextPage();
            loadDataForCurrentPage();
        }
    }

    public boolean hasMoreData() {
        return paginationState.hasMoreData();
    }

    public void setHasMoreData(boolean hasMoreData) {
        paginationState.setHasMoreData(hasMoreData);
    }

    public boolean isFirstPage() {
        return paginationState.isFirstPage();
    }

    private void loadPodcastData(UserDataRequest request) {
        if (paginationState.getCurrentPage() == 0) {
            GlobalEventBus.getInstance().post(new GetPodcastByIdRequest(paginationState.getCurrentPodcastId()));
        }
        GlobalEventBus.getInstance().post(new GetEpisodeByPodcastIdRequest(request));
    }

    private void loadPageData(UserDataRequest request) {
        String pageType = paginationState.getCurrentPageType();
        switch (pageType) {
            case "Queue" -> GlobalEventBus.getInstance().post(new GetQueueAllRequest(request));
            case "Inbox" -> GlobalEventBus.getInstance().post(new GetInboxAllRequest(request));
            case "Downloads" -> GlobalEventBus.getInstance().post(new GetDownloadAllRequest(request));
            case "Subscriptions" -> GlobalEventBus.getInstance().post(new GetSubscriptionsRequest(request));
            case "Playback history" -> GlobalEventBus.getInstance().post(new GetHistoryAllRequest(request));
            case "Episodes" -> GlobalEventBus.getInstance().post(new GetEpisodeAllRequest(request));
            case "Home" -> loadDataHomePage();
            default -> LOGGER.warn("Type de page non reconnu: {}", pageType);
        }
    }

    private void loadDataHomePage() {
        paginationState.setHasMoreData(false);
        this.pageContentRenderer.updateHomePage();
        GlobalEventBus.getInstance().post(new GetInboxTop8Request());
        GlobalEventBus.getInstance().post(new GetDownloadTop8Request());
        GlobalEventBus.getInstance().post(new GetQueueTop8Request());
        GlobalEventBus.getInstance().post(new GetPodcastReadTop8Request());
        GlobalEventBus.getInstance().post(new GetSurpriseRequest());
    }

    private UserDataRequest createUserDataRequest() {
        return new UserDataRequest(
                paginationState.getCurrentPodcastId(),
                paginationState.getCurrentPage() * PAGE_SIZE,
                PAGE_SIZE
        );
    }
}

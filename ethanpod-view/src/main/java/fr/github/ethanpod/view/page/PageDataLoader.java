package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.view.util.PaginationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

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
        LOGGER.debug("Chargement page: {}", paginationState.getCurrentPage());

        if (paginationState.hasPodcastId()) {
            loadPodcastData();
        } else {
            loadPageData();
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

    private void loadPodcastData() {
        if (paginationState.getCurrentPage() < 1) {
            sendRequest(UserRequestType.GET_PODCAST_BY_ID, "PODCAST", paginationState.getCurrentPodcastId());
        }
        sendRequest(UserRequestType.GET_EPISODE_BY_PODCAST_ID, "EPISODE", createUserDataRequest());
    }

    private void loadPageData() {
        String pageType = paginationState.getCurrentPageType();
        UserDataRequest request = createUserDataRequest();

        switch (pageType) {
            case "Queue" -> sendRequest(UserRequestType.GET_QUEUE_ALL, "QUEUE", request);
            case "Inbox" -> sendRequest(UserRequestType.GET_INBOX_ALL, "INBOX", request);
            case "Downloads" -> sendRequest(UserRequestType.GET_DOWNLOAD_ALL, "DOWNLOAD", request);
            case "Subscriptions" -> sendRequest(UserRequestType.GET_SUBSCRIPTION_ALL, "PODCAST", request);
            case "Playback history" -> sendRequest(UserRequestType.GET_HISTORY_ALL, "HISTORY", request);
            case "Episodes" -> sendRequest(UserRequestType.GET_EPISODE_ALL, "EPISODE", request);
            case "Home" -> loadDataHomePage();
            default -> LOGGER.warn("Type de page non reconnu: {}", pageType);
        }
    }

    private void loadDataHomePage() {
        paginationState.setHasMoreData(false);
        this.pageContentRenderer.updateHomePage();
        sendRequest(UserRequestType.GET_INBOX_TOP8, "INBOX", null);
        sendRequest(UserRequestType.GET_DOWNLOAD_TOP8, "DOWNLOAD", null);
        sendRequest(UserRequestType.GET_QUEUE_TOP8, "QUEUE", null);
        sendRequest(UserRequestType.GET_PODCAST_READ_TOP8, "PODCAST", null);
    }

    private void sendRequest(UserRequestType type, String service, Object data) {
        String id = "[" + service + "]" + UUID.randomUUID();
        MessageRouter.getInstance().userRequest(type, id, data);
    }

    private UserDataRequest createUserDataRequest() {
        return new UserDataRequest(
                paginationState.getCurrentPodcastId(),
                paginationState.getCurrentPage() * PAGE_SIZE,
                PAGE_SIZE
        );
    }
}

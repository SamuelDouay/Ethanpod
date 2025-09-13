package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.util.manager.BaseServiceManager;
import fr.github.ethanpod.util.manager.ServiceConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerManager extends BaseServiceManager<Controller> {
    protected static final Logger logger = LogManager.getLogger(ControllerManager.class);
    private final AsyncServiceManager asyncServiceManager;

    public ControllerManager(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
        initializeServices();
    }

    private void initializeServices() {
        registerService(ServiceConstants.NAVIGATION_SERVICE, new NavigationController(asyncServiceManager));
        registerService(ServiceConstants.INBOX_SERVICE, new InboxController(asyncServiceManager));
        registerService(ServiceConstants.QUEUE_SERVICE, new QueueController(asyncServiceManager));
        registerService(ServiceConstants.PODCAST_SERVICE, new PodcastController(asyncServiceManager));
        registerService(ServiceConstants.DOWNLOAD_SERVICE, new DownloadController(asyncServiceManager));
        registerService(ServiceConstants.EPISODE_SERVICE, new EpisodeController(asyncServiceManager));
        registerService(ServiceConstants.HISTORY_SERVICE, new HistoryController(asyncServiceManager));
        registerService(ServiceConstants.SURPRISE_SERVICE, new SurpriseController(asyncServiceManager));
    }

    private PodcastController getPodcastController() {
        return getService(ServiceConstants.PODCAST_SERVICE, PodcastController.class);
    }

    private EpisodeController getEpisodeController() {
        return getService(ServiceConstants.EPISODE_SERVICE, EpisodeController.class);
    }

    private NavigationController getNavigationController() {
        return getService(ServiceConstants.NAVIGATION_SERVICE, NavigationController.class);
    }

    private InboxController getInboxController() {
        return getService(ServiceConstants.INBOX_SERVICE, InboxController.class);
    }

    private QueueController getQueueController() {
        return getService(ServiceConstants.QUEUE_SERVICE, QueueController.class);
    }

    private DownloadController getDownloadController() {
        return getService(ServiceConstants.DOWNLOAD_SERVICE, DownloadController.class);
    }

    private HistoryController getHistoryController() {
        return getService(ServiceConstants.HISTORY_SERVICE, HistoryController.class);
    }

    private SurpriseController getSurpriseController() {
        return getService(ServiceConstants.SURPRISE_SERVICE, SurpriseController.class);
    }

    public void handleUserRequest(ThreadMessage message) {
        UserRequestType type = (UserRequestType) message.type();

        switch (type) {
            case GET_PODCAST_BY_ID -> getPodcastController().getPodcastById(message);
            case GET_EPISODE_BY_PODCAST_ID -> getEpisodeController().getEpisodeByPodcastId(message);
            case GET_NAVIGATION_LIST -> getNavigationController().loadNavigationData(message);
            case GET_INBOX_COUNT -> getInboxController().loadInboxCount(message);
            case GET_INBOX_TOP8 -> getInboxController().loadInboxTop8(message);
            case GET_DOWNLOAD_TOP8 -> getDownloadController().loadDownloadTop8(message);
            case GET_QUEUE_TOP8 -> getQueueController().loadQueueTop8(message);
            case GET_PODCAST_READ_TOP8 -> getPodcastController().loadTop8PodcastRead(message);
            case GET_QUEUE_ALL -> getQueueController().loadQueueAll(message);
            case GET_INBOX_ALL -> getInboxController().loadInboxAll(message);
            case GET_DOWNLOAD_ALL -> getDownloadController().loadDownloadAll(message);
            case GET_HISTORY_ALL -> getHistoryController().loadHistoryAll(message);
            case GET_SUBSCRIPTION_ALL -> getPodcastController().getSubscriptionAll(message);
            case GET_EPISODE_ALL -> getEpisodeController().getEpisodeAll(message);
            case GET_SURPRISE_ALL -> getSurpriseController().loadSurpriseData(message);
            default -> logger.warn("Type de message non géré: {}", message.type());
        }
    }
}

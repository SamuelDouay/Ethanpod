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
    }

    public PodcastController getPodcastController() {
        return getService(ServiceConstants.PODCAST_SERVICE, PodcastController.class);
    }

    public void handleUserRequest(ThreadMessage message) {
        UserRequestType type = (UserRequestType) message.type();

        switch (type) {
            case UserRequestType.GET_FEED_BY_ID -> getPodcastController().getPodcastById((Integer) message.data());
            default -> logger.warn("Type de message non géré: {}", message.type());
        }
    }


    public void initializeAllServices() {
        getAllServices().forEach(Controller::initializeUI);
    }
}

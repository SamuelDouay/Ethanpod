package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.NotificationType;
import fr.github.ethanpod.core.thread.RequestType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.service.DataServiceManager;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class LogicHandle {
    private static final Logger logger = LogManager.getLogger(LogicHandle.class);
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final DataServiceManager serviceManager;

    public LogicHandle(BlockingQueue<ThreadMessage> messageQueue, ExecutorService service, DatabaseManager databaseManager) {
        this.messageQueue = messageQueue;
        this.serviceManager = new DataServiceManager(service, databaseManager);
    }

    public void processIncomingMessages() throws InterruptedException {
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.debug(message);
            switch (message.category()) {
                case REQUEST -> handleRequest(message);
                case NOTIFICATION -> handleNotification(message);
                default -> logger.warn("Type de message non géré: {}", message.type());
            }
        }
    }

    private void handleRequest(ThreadMessage message) {
        RequestType content = (RequestType) message.type();
        String requestId = message.id();

        logger.debug("Traitement requête: {} avec ID: {}", content, requestId);

        switch (content) {
            case GET_NAVIGATION_LIST -> serviceManager.getNavigationService().getNavigationListAsync(requestId);
            case GET_INBOX_COUNT -> serviceManager.getInboxService().getInboxCountAsync(requestId);
            case GET_INBOX_TOP8 -> serviceManager.getInboxService().getTop8InInbox(requestId);
            case GET_QUEUE_TOP8 -> serviceManager.getQueueService().getQueueTop8(requestId);
            case GET_PODCAST_READ_TOP8 -> serviceManager.getPodcastService().getTop8PodcastRead(requestId);
            case GET_DOWNLOAD_TOP8 -> serviceManager.getDownloadService().getDownloadTop8(requestId);
            case GET_PODCAST_BY_ID ->
                    serviceManager.getPodcastService().getPodcastById(requestId, (Integer) message.data());
            case GET_EPISODE_BY_PODCAST_ID ->
                    serviceManager.getEpisodeService().getEpisodeByPodcastId(requestId, (UserDataRequest) message.data());
            case GET_QUEUE_ALL ->
                    serviceManager.getQueueService().getAllInQueue(requestId, (UserDataRequest) message.data());
            case GET_INBOX_ALL ->
                    serviceManager.getInboxService().getAllInInbox(requestId, (UserDataRequest) message.data());
            case GET_DOWNLOAD_ALL ->
                    serviceManager.getDownloadService().getDownloadAll(requestId, (UserDataRequest) message.data());
            case GET_HISTORY_ALL ->
                    serviceManager.getHistoryService().getAllInHistory(requestId, (UserDataRequest) message.data());
            case GET_SUBSCRIPTION_ALL ->
                    serviceManager.getPodcastService().getAllSusbcription(requestId, (UserDataRequest) message.data());
            case GET_EPISODE_ALL ->
                    serviceManager.getEpisodeService().getEpisodeAll(requestId, (UserDataRequest) message.data());
            default -> logger.warn("Requête non reconnue: {}", content);

        }
    }

    private void handleNotification(ThreadMessage message) {
        if (message.type().equals(NotificationType.UI_READY)) {
            messageRouter.sendNotification(MessageRouter.LOGIC_THREAD, MessageRouter.VIEW_THREAD, NotificationType.LOGIC_READY);
        }
    }

    public void refreshData() {
        serviceManager.refreshAllData();
    }
}
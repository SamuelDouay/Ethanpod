package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.service.DataServiceManager;
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

    public LogicHandle(BlockingQueue<ThreadMessage> messageQueue, ExecutorService service) {
        this.messageQueue = messageQueue;
        this.serviceManager = new DataServiceManager(service);
    }

    public void processIncomingMessages() throws InterruptedException {
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.info("🔵 {}", message);

            switch (message.getType()) {
                case REQUEST -> handleRequest(message);
                case NOTIFICATION -> handleNotification(message);
                default -> logger.warn("🔵 Type de message non géré: {}", message.getType());
            }
        }
    }

    private void handleRequest(ThreadMessage message) {
        String content = message.getContent();
        String requestId = message.getRequestId();

        logger.info("🔵 Traitement requête: {} avec ID: {}", content, requestId);

        switch (content) {
            case "GET_NAVIGATION_LIST" -> serviceManager.getNavigationService().getNavigationListAsync(requestId);
            case "INBOX_COUNT" -> serviceManager.getInboxService().getInboxCountAsync(requestId);
            case "GET_TOP8_QUEUE" -> serviceManager.getQueueService().getQueueTop8(requestId);
            case "REFRESH_DATA" -> serviceManager.refreshAllData();
            default -> {
                logger.warn("🔵 Requête non reconnue: {}", content);
                messageRouter.sendRequestToViewFromLogic("ERROR", requestId, MessageType.ERROR, "Unknown request: " + content);
            }
        }
    }

    private void handleNotification(ThreadMessage message) {
        if ("UI_READY".equals(message.getContent())) {
            messageRouter.sendRequestToViewFromLogic("LOGIC_READY", null, MessageType.NOTIFICATION, null);
        }
    }

    public void refreshData() {
        serviceManager.refreshAllData();
    }
}
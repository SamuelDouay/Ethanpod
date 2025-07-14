package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.service.DataServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class LogicHandler {
    private static final Logger logger = LogManager.getLogger(LogicHandler.class);
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final DataServiceManager serviceManager;

    public LogicHandler(BlockingQueue<ThreadMessage> messageQueue, ExecutorService service) {
        this.messageQueue = messageQueue;
        this.serviceManager = new DataServiceManager(service);
    }

    public void processIncomingMessages() {
        ThreadMessage message = messageQueue.poll();

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
            case "REFRESH_DATA" -> serviceManager.refreshAllData();
            default -> {
                logger.warn("🔵 Requête non reconnue: {}", content);
                messageRouter.sendRequestToView("ERROR", requestId, MessageType.ERROR, "Unknown request: " + content);
            }
        }
    }

    private void handleNotification(ThreadMessage message) {
        if ("UI_READY".equals(message.getContent())) {
            messageRouter.sendRequestToView("LOGIC_READY", null, MessageType.NOTIFICATION, null);
        }
    }

    public void refreshData() {
        serviceManager.refreshAllData();
    }
}
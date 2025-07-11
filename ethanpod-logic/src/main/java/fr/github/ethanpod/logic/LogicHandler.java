package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.services.LogicDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class LogicHandler {
    private static final Logger logger = LogManager.getLogger(LogicHandler.class);
    private final LogicDataService dataService;
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    private final BlockingQueue<ThreadMessage> messageQueue;

    public LogicHandler(LogicDataService dataService, BlockingQueue<ThreadMessage> messageQueue) {
        this.dataService = dataService;
        this.messageQueue = messageQueue;
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
            case "GET_NAVIGATION_LIST" -> dataService.getNavigationListAsync(requestId);
            case "GET_INBOX_COUNT", "INBOX_COUNT_REQUEST" -> dataService.getInboxCountAsync(requestId);
            case "REFRESH_DATA" -> dataService.refreshNavigationDataAsync();
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
        dataService.refreshNavigationDataAsync();
    }
}
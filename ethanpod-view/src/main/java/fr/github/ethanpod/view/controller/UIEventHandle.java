package fr.github.ethanpod.view.controller;


import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UIEventHandle {
    private static final Logger logger = LogManager.getLogger(UIEventHandle.class);
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final UIControllerManager uiControllerManager;
    private final MessageRouter messageRouter = MessageRouter.getInstance();

    public UIEventHandle() {
        this.messageQueue = MessageRouter.getInstance().registerThread("UIEventThread");
        this.uiControllerManager = new UIControllerManager();
    }

    public void processIncomingMessages() throws InterruptedException {
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            if (Objects.requireNonNull(message.getType()) == MessageType.EVENT) {
                handleEvent(message);
            } else {
                logger.warn("🔵 Type de message non géré: {}", message.getType());
            }
        }
    }

    private void handleEvent(ThreadMessage message) {
        String content = message.getContent();
        String requestId = message.getRequestId();

        logger.info("🔵 Traitement requête: {} avec ID: {}", content, requestId);

        switch (content) {
            case "NAVIGATION_UPDATED" ->
                    uiControllerManager.getNavigationService().updateNavigationUI((List<NavigationItem>) message.getData());
            case "INBOX_COUNT_UPDATED" ->
                    uiControllerManager.getInboxService().updateInboxCount((Integer) message.getData());
            case "GET_TOP8_QUEUE_UPDATE" ->
                    uiControllerManager.getQueueService().updateQueueTop8UI((List<EpisodeItem>) message.getData());
            case "GET_TOP8_INBOX_UPDATE" ->
                    uiControllerManager.getInboxService().updateInboxTop8((List<EpisodeItem>) message.getData());
            default -> {
                logger.warn("🔵 Requête non reconnue: {}", content);
                messageRouter.sendRequestToViewFromEvent("ERROR", requestId, MessageType.ERROR, "Unknown request: " + content);
            }
        }
    }
}

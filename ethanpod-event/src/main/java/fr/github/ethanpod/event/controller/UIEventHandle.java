package fr.github.ethanpod.event.controller;


import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageCategory;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UIEventHandle {
    private static final Logger logger = LogManager.getLogger(UIEventHandle.class);
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final UIControllerManager uiControllerManager;

    public UIEventHandle() {
        this.messageQueue = MessageRouter.getInstance().registerThread("UIEventThread");
        this.uiControllerManager = new UIControllerManager();
    }

    public void processIncomingMessages() throws InterruptedException {
        ThreadMessage message = messageQueue.poll(500, TimeUnit.MILLISECONDS);

        if (message != null) {
            logger.debug(message);
            if (message.category() == MessageCategory.EVENT) {
                handleEvent(message);
            } else {
                logger.warn("Type de message non géré: {}", message.type());
            }
        }
    }

    private void handleEvent(ThreadMessage message) {
        EventType content = (EventType) message.type();
        String requestId = message.id();

        logger.debug("Traitement requête: {} avec ID: {}", content, requestId);


        switch (content) {
            case NAVIGATION_UPDATED ->
                    uiControllerManager.getNavigationService().updateNavigationUI((List<NavigationItem>) message.data());
            case INBOX_COUNT_UPDATED ->
                    uiControllerManager.getInboxService().updateInboxCount((Integer) message.data());
            case QUEUE_TOP8_UPDATED ->
                    uiControllerManager.getQueueService().updateQueueTop8UI((List<EpisodeItem>) message.data());
            case INBOX_TOP8_UPDATED ->
                    uiControllerManager.getInboxService().updateInboxTop8((List<EpisodeItem>) message.data());
            case PODCAST_TOP8_UPDATED ->
                    uiControllerManager.getPodcastService().updatePodcastTop8UI((List<EpisodeItem>) message.data());
            default -> logger.warn("Requête non reconnue: {}", content);

        }
    }
}

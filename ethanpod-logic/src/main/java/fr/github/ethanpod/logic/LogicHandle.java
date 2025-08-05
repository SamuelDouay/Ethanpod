package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.*;
import fr.github.ethanpod.logic.service.DataServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogicHandle {
    private static final Logger logger = LogManager.getLogger(LogicHandle.class);
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final DataServiceManager serviceManager;
    private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);

    public LogicHandle(BlockingQueue<ThreadMessage> messageQueue, ExecutorService service) {
        this.messageQueue = messageQueue;
        this.serviceManager = new DataServiceManager(service);
    }

    public void processIncomingMessages() throws InterruptedException {
        if (shutdownRequested.get()) {
            logger.debug("Arrêt en cours");
            return;
        }

        try {
            ThreadMessage message = messageQueue.take();
            logger.debug(message);
            switch (message.getCategory()) {
                case REQUEST -> handleRequest(message);
                case NOTIFICATION -> handleNotification(message);
                default -> logger.warn("Type de message non géré: {}", message.getType());
            }
        } catch (InterruptedException e) {
            logger.debug("Thread interrompu");
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    private void handleRequest(ThreadMessage message) {
        if (shutdownRequested.get()) {
            logger.debug("Requête ignorée, shutdown en cours");
            return;
        }

        RequestType content = (RequestType) message.getType();
        String requestId = message.getId();

        logger.debug("Traitement requête: {} avec ID: {}", content, requestId);

        try {
            switch (content) {
                case GET_NAVIGATION_LIST -> serviceManager.getNavigationService().getNavigationListAsync(requestId);
                case GET_INBOX_COUNT -> serviceManager.getInboxService().getInboxCountAsync(requestId);
                case GET_INBOX_TOP8 -> serviceManager.getInboxService().getTop8InInbox(requestId);
                case GET_QUEUE_TOP8 -> serviceManager.getQueueService().getQueueTop8(requestId);
                case GET_PODCAST_READ_TOP8 -> serviceManager.getPodcastService().getTop8PodcastRead(requestId);
                default -> logger.warn("Requête non reconnue: {}", content);
            }
        } catch (Exception e) {
            logger.error("Erreur lors du traitement de la requête {}: {}", content, e.getMessage(), e);
        }
    }

    private void handleNotification(ThreadMessage message) {
        if (message.getType().equals(NotificationType.UI_READY)) {
            messageRouter.sendNotification(MessageRouter.LOGIC_THREAD, MessageRouter.VIEW_THREAD, NotificationType.LOGIC_READY);
        }
    }

    public void refreshData() {
        if (!shutdownRequested.get()) {
            serviceManager.refreshAllData();
        }
    }


    public void flushPendingMessages() {
        logger.debug("Traitement des messages restants...");
        int processedCount = 0;

        try {
            while (!messageQueue.isEmpty() && processedCount < 10) { // Limite de sécurité
                ThreadMessage message = messageQueue.poll();
                if (message != null) {
                    logger.debug("Message final: {}", message.getType());

                    // Traiter seulement les responses critiques
                    if (message.getCategory() == MessageCategory.REQUEST) {
                        handleRequest(message);
                    }
                    processedCount++;
                }
            }
            logger.debug("{} messages restants traités", processedCount);
        } catch (Exception e) {
            logger.warn("Erreur lors du flush: {}", e.getMessage());
        }
    }


    public void sendShutdownSignal() {
        shutdownRequested.set(true);
    }

}
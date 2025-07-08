package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LogicThread implements Runnable {
    private final Logger logger = LogManager.getLogger(LogicThread.class);
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final MessageRouter messageRouter;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    private final NavigationDao navigationDao;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ExecutorService asyncExecutor = Executors.newCachedThreadPool(
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("LogicThread-" + threadNumber.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
            }
    );

    public LogicThread() {
        this.messageRouter = MessageRouter.getInstance();
        this.messageQueue = this.messageRouter.registerThread("LogicThread");
        this.navigationDao = new NavigationDao();
    }

    @Override
    public void run() {
        logger.info("🔵 Thread Logique démarré - Traitement des données");

        try {
            startPeriodicTasks();

            while (running.get()) {
                try {
                    processIncomingMessages();
                    Thread.sleep(100); // Petite pause pour éviter la surcharge CPU
                } catch (InterruptedException _) {
                    logger.info("🔵 Thread Logique interrompu");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("🔵 Erreur dans le thread de logique", e);
                }
            }
        } finally {
            scheduler.shutdown();
            asyncExecutor.shutdown();
            logger.info("🔵 Thread Logique terminé");
        }
    }

    private void startPeriodicTasks() {
        scheduler.scheduleAtFixedRate(() -> {
            if (running.get()) {
                refreshNavigationDataAsync();
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    private void processIncomingMessages() {
        ThreadMessage message = messageQueue.poll();

        if (message != null) {
            logger.info("🔵 Message reçu: De: {}, Type: {}, Contenu: {}",
                    message.getSender(), message.getType(), message.getContent());

            switch (message.getType()) {
                case REQUEST -> {
                    logger.info("🔵 Traitement REQUEST: {}", message.getContent());
                    handleRequest(message);
                }
                case NOTIFICATION -> {
                    logger.info("🔵 Traitement NOTIFICATION: {}", message.getContent());
                    handleNotification(message);
                }
                default -> logger.warn("🔵 Type de message non géré: {}", message.getType());
            }
        }
    }

    private void handleRequest(ThreadMessage message) {
        String content = message.getContent();
        String requestId = message.getRequestId();

        logger.info("🔵 Traitement requête: {} avec ID: {}", content, requestId);

        switch (content) {
            case "GET_NAVIGATION_LIST" -> {
                logger.info("🔵 Démarrage GET_NAVIGATION_LIST pour ID: {}", requestId);
                getNavigationListAsync(requestId);
            }
            case "GET_INBOX_COUNT", "INBOX_COUNT_REQUEST" -> {
                logger.info("🔵 Démarrage GET_INBOX_COUNT pour ID: {}", requestId);
                getInboxCountAsync(requestId);
            }
            case "REFRESH_DATA" -> {
                logger.info("🔵 Démarrage REFRESH_DATA");
                refreshNavigationDataAsync();
            }
            default -> {
                logger.warn("🔵 Requête non reconnue: {}", content);
                sendMessage("ERROR: Unknown request: " + content, MessageType.ERROR, null, requestId);
            }
        }
    }

    private void handleNotification(ThreadMessage message) {
        logger.info("🔵 Notification reçue: {}", message.getContent());

        if ("UI_READY".equals(message.getContent())) {
            sendMessage("LOGIC_READY", MessageType.NOTIFICATION, null, null);
        }
    }

    public void stop() {
        logger.info("🔵 Arrêt du thread de logique demandé");
        running.set(false);
        scheduler.shutdown();
        asyncExecutor.shutdown();
    }

    // ================================
    // Fonctions Asynchrones avec CompletableFuture
    // ================================

    public void getNavigationListAsync(String requestId) {
        int opId = requestCounter.incrementAndGet();

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        logger.info("🔵 Logique [{}]: Récupération liste navigation", opId);
                        logger.info("🔵 Logique : requete id {}", requestId);

                        List<NavigationItem> navigationList = navigationDao.getList();
                        Thread.sleep(500); // remove

                        logger.info("🔵 Logique [{}]: {} éléments récupérés", opId, navigationList.size());
                        return navigationList;

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted while getting navigation list", e);
                    } catch (Exception e) {
                        throw new RuntimeException("Error getting navigation list", e);
                    }
                }, asyncExecutor)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Erreur lors de la récupération de la liste de navigation", throwable);
                        sendMessage("ERROR: " + throwable.getMessage(), MessageType.ERROR, null, requestId);
                    } else {
                        sendMessage("NAVIGATION_LIST_RESULT", MessageType.RESPONSE, result, requestId);
                    }
                });
    }

    public void getInboxCountAsync(String requestId) {
        int opId = requestCounter.incrementAndGet();

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        logger.info("🔵 Logique [{}]: Récupération nombre inbox", opId);
                        int count = navigationDao.getNumberOfInbox();
                        logger.info("🔵 Logique [{}]: {} éléments dans inbox", opId, count);
                        return count;
                    } catch (Exception e) {
                        throw new RuntimeException("Error getting inbox count", e);
                    }
                }, asyncExecutor)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Erreur lors de la récupération du nombre d'inbox", throwable);
                        sendMessage("ERROR: " + throwable.getMessage(), MessageType.ERROR, null, requestId);
                    } else {
                        sendMessage("INBOX_COUNT_RESPONSE", MessageType.RESPONSE, result, requestId);
                    }
                });
    }

    private void refreshNavigationDataAsync() {
        logger.info("🔵 Logique: Rafraîchissement automatique des données");

        CompletableFuture<List<NavigationItem>> navigationFuture =
                CompletableFuture.supplyAsync(() -> navigationDao.getList(), asyncExecutor);

        CompletableFuture<Integer> inboxCountFuture =
                CompletableFuture.supplyAsync(() -> navigationDao.getNumberOfInbox(), asyncExecutor);

        CompletableFuture.allOf(navigationFuture, inboxCountFuture)
                .thenRun(() -> {
                    try {
                        List<NavigationItem> updatedList = navigationFuture.get();
                        int inboxCount = inboxCountFuture.get();

                        // Notifier l'interface des nouvelles données
                        sendMessage("DATA_REFRESHED", MessageType.DATA_UPDATE, updatedList, null);
                        sendMessage("INBOX_UPDATED", MessageType.DATA_UPDATE, inboxCount, null);

                        logger.info("🔵 Logique: Données rafraîchies - {} éléments, {} inbox",
                                updatedList.size(), inboxCount);
                    } catch (Exception e) {
                        logger.error("Erreur lors du rafraîchissement des données", e);
                    }
                });
    }

    private void sendMessage(String content, MessageType type, Object data, String requestId) {
        ThreadMessage message = new ThreadMessage(content, "LogicThread", "ViewThread",
                type, data, requestId);
        boolean success = messageRouter.routeMessage(message);
        if (!success) {
            logger.error("🔵 Échec de l'envoi du message vers ViewThread: {}", content);
        } else {
            logger.info("🔵 Message envoyé avec succès vers ViewThread: {}", content);
        }
    }
}
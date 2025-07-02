package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LogicThread implements Runnable {
    private final Logger logger = LogManager.getLogger(LogicThread.class);

    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ThreadMessage> messageQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    private final NavigationDao navigationDao;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public LogicThread(BlockingQueue<ThreadMessage> messageQueue) {
        this.messageQueue = messageQueue;
        this.navigationDao = new NavigationDao();
    }

    @Override
    public void run() {
        logger.info("🔵 Thread Logique démarré - Traitement des données");

        try {
            startPeriodicTasks();

            while (running.get()) {
                try {
                    // CORRECTION 1: Traiter les messages AVANT les tâches pour prioriser les requêtes
                    processIncomingMessages();

                    // Traiter les tâches en attente avec un timeout plus court
                    Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        logger.debug("🔵 Exécution d'une tâche de la queue");
                        task.run();
                    }

                } catch (InterruptedException e) {
                    logger.info("🔵 Thread Logique interrompu");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("🔵 Erreur dans le thread de logique", e);
                }
            }
        } finally {
            scheduler.shutdown();
            logger.info("🔵 Thread Logique terminé");
        }
    }

    private void startPeriodicTasks() {
        // Tâche de rafraîchissement automatique des données
        scheduler.scheduleAtFixedRate(() -> {
            if (running.get()) {
                refreshNavigationData();
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    private void processIncomingMessages() {
        ThreadMessage message = messageQueue.poll();

        if (message != null) {
            logger.info("🔵 Message trouvé dans la queue - De: {}, Pour: {}, Type: {}, Contenu: {}",
                    message.getSender(), message.getReceiver(), message.getType(), message.getContent());

            // CORRECTION PRINCIPALE: Vérifier le bon destinataire
            if ("LogicThread".equals(message.getReceiver())) {
                logger.info("🔵 Logique reçu: {}", message);

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
            } else {
                logger.debug("🔵 Message ignoré (destinataire: {})", message.getReceiver());
            }
        }
    }

    private void handleRequest(ThreadMessage message) {
        String content = message.getContent();
        String requestId = message.getRequestId();

        logger.info("🔵 Traitement requête: '{}' avec ID: {}", content, requestId);

        switch (content) {
            case "GET_NAVIGATION_LIST" -> {
                logger.info("🔵 Démarrage GET_NAVIGATION_LIST pour ID: {}", requestId);
                getNavigationListAsync(requestId);
            }
            case "GET_INBOX_COUNT" -> {
                logger.info("🔵 Démarrage GET_INBOX_COUNT pour ID: {}", requestId);
                getInboxCountAsync(requestId);
            }
            case "REFRESH_DATA" -> {
                logger.info("🔵 Démarrage REFRESH_DATA");
                refreshNavigationData();
            }
            default -> {
                logger.warn("🔵 Requête non reconnue: '{}'", content);
                // Envoyer une erreur en retour
                sendMessage("ERROR: Unknown request: " + content, MessageType.ERROR, null, requestId);
            }
        }
    }

    private void handleNotification(ThreadMessage message) {
        logger.info("🔵 Notification reçue: {}", message.getContent());

        // Répondre aux notifications importantes
        if ("UI_READY".equals(message.getContent())) {
            sendMessage("LOGIC_READY", MessageType.NOTIFICATION, null, null);
            // Envoyer les données initiales
            getNavigationListAsync(UUID.randomUUID().toString());
        }
    }

    public void stop() {
        logger.info("🔵 Arrêt du thread de logique demandé");
        running.set(false);
        scheduler.shutdown();
    }


    // ================================
    // Fonctions Asynchrones de Logique
    // ================================

    public CompletableFuture<List<NavigationItem>> getNavigationListAsync(String requestId) {
        CompletableFuture<List<NavigationItem>> future = new CompletableFuture<>();
        int opId = requestCounter.incrementAndGet();

        taskQueue.offer(() -> {
            try {
                logger.info("🔵 Logique [{}]: Récupération liste navigation", opId);
                logger.info("🔵 Logique : requete id {}", requestId);

                List<NavigationItem> navigationList = navigationDao.getList();

                // Simulation de traitement
                Thread.sleep(500);

                logger.info("🔵 Logique [{}]: {} éléments récupérés", opId, navigationList.size());

                // Envoyer le résultat au thread View
                sendMessage("NAVIGATION_LIST_RESULT", MessageType.RESPONSE,
                        navigationList, requestId);

                future.complete(navigationList);

            } catch (Exception e) {
                logger.error("Erreur lors de la récupération de la liste de navigation", e);
                sendMessage("ERROR: " + e.getMessage(), MessageType.ERROR,
                        null, requestId);
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public CompletableFuture<Integer> getInboxCountAsync(String requestId) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        int opId = requestCounter.incrementAndGet();

        taskQueue.offer(() -> {
            try {
                logger.info("🔵 Logique [{}]: Récupération nombre inbox", opId);

                int count = navigationDao.getNumberOfInbox();

                logger.info("🔵 Logique [{}]: {} éléments dans inbox", opId, count);

                sendMessage("INBOX_COUNT_RESULT", MessageType.RESPONSE,
                        count, requestId);

                future.complete(count);

            } catch (Exception e) {
                logger.error("Erreur lors de la récupération du nombre d'inbox", e);
                sendMessage("ERROR: " + e.getMessage(), MessageType.ERROR,
                        null, requestId);
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    private void refreshNavigationData() {
        logger.info("🔵 Logique: Rafraîchissement automatique des données");

        taskQueue.offer(() -> {
            try {
                List<NavigationItem> updatedList = navigationDao.getList();
                int inboxCount = navigationDao.getNumberOfInbox();

                // Notifier l'interface des nouvelles données
                sendMessage("DATA_REFRESHED", MessageType.DATA_UPDATE,
                        updatedList, null);
                sendMessage("INBOX_UPDATED", MessageType.DATA_UPDATE,
                        inboxCount, null);

                logger.info("🔵 Logique: Données rafraîchies - {} éléments, {} inbox",
                        updatedList.size(), inboxCount);

            } catch (Exception e) {
                logger.error("Erreur lors du rafraîchissement des données", e);
            }
        });
    }

    private void sendMessage(String content, MessageType type, Object data, String requestId) {
        try {
            ThreadMessage message = new ThreadMessage(content, "LogicThread", "ViewThread",
                    type, data, requestId);
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Erreur lors de l'envoi du message", e);
        }
    }
}

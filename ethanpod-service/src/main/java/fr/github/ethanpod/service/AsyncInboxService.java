package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncInboxService implements AsyncService {
    private static final Logger logger = LogManager.getLogger(AsyncInboxService.class);
    private static final String SERVICE_ID = "inbox";

    private final MessageRouter messageRouter;
    private final ConcurrentHashMap<String, CompletableFuture<Integer>> pendingRequests = new ConcurrentHashMap<>();
    private final AtomicBoolean active = new AtomicBoolean(false);

    public AsyncInboxService() {
        this.messageRouter = MessageRouter.getInstance();
    }

    @Override
    public void initialize() {
        logger.info("Initialisation du service Inbox");
        active.set(true);
    }

    @Override
    public void handleResponse(ThreadMessage message) {
        String content = message.getContent();

        if (content.startsWith("INBOX_COUNT_RESPONSE")) {
            handleInboxCountResponse(message);
        } else if (content.startsWith("INBOX_REFRESH_RESPONSE")) {
            handleInboxRefreshResponse(message);
        } else {
            logger.warn("Réponse inbox non gérée: {}", content);
        }
    }

    private void handleInboxCountResponse(ThreadMessage message) {
        String requestId = extractRequestId(message.getContent());
        CompletableFuture<Integer> future = pendingRequests.remove(requestId);

        if (future != null) {
            Integer count = (Integer) message.getData();
            future.complete(count);
            logger.debug("Réponse inbox count traitée: {}", count);
        }
    }

    private void handleInboxRefreshResponse(ThreadMessage message) {
        logger.info("Données inbox rafraîchies");
        // Traiter la réponse de rafraîchissement
    }

    /**
     * Obtient le nombre d'éléments dans l'inbox de manière asynchrone
     */
    public CompletableFuture<Integer> getInboxCountAsync() {
        if (!active.get()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Service inbox non actif"));
        }

        String requestId = generateRequestId();
        CompletableFuture<Integer> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        ThreadMessage message = new ThreadMessage(
                "INBOX_COUNT_REQUEST",
                "ViewThread",
                "LogicThread",
                MessageType.REQUEST,
                null,
                requestId
        );

        boolean success = messageRouter.routeMessage(message);
        if (!success) {
            pendingRequests.remove(requestId);
            future.completeExceptionally(new RuntimeException("Échec de l'envoi de la requête"));
        }

        return future;
    }

    /**
     * Marque un élément de l'inbox comme lu
     */
    public CompletableFuture<Boolean> markAsReadAsync(String itemId) {
        if (!active.get()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Service inbox non actif"));
        }

        String requestId = generateRequestId();
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        ThreadMessage message = new ThreadMessage(
                "INBOX_MARK_READ_REQUEST",
                "ViewThread",
                "LogicThread",
                MessageType.REQUEST,
                itemId,
                requestId
        );

        boolean success = messageRouter.routeMessage(message);
        if (!success) {
            future.completeExceptionally(new RuntimeException("Échec de l'envoi de la requête"));
        }

        return future;
    }

    @Override
    public void refreshData() {
        if (!active.get()) {
            logger.warn("Tentative de rafraîchissement d'un service inbox inactif");
            return;
        }

        ThreadMessage message = new ThreadMessage(
                "INBOX_REFRESH_REQUEST",
                "ViewThread",
                "LogicThread",
                MessageType.REQUEST,
                null,
                null
        );

        messageRouter.routeMessage(message);
        logger.info("Demande de rafraîchissement des données inbox envoyée");
    }

    @Override
    public void stop() {
        logger.info("Arrêt du service Inbox");
        active.set(false);
        pendingRequests.clear();

        //pendingRequests.values().forEach(future ->
        //        future.completeExceptionally(new RuntimeException("Service arrêté"))
        //);
    }

    @Override
    public String getServiceId() {
        return SERVICE_ID;
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    private String generateRequestId() {
        return "[" + SERVICE_ID + "]" + UUID.randomUUID();
    }

    private String extractRequestId(String content) {
        String[] parts = content.split("_");
        if (parts.length >= 3) {
            return parts[parts.length - 2] + "_" + parts[parts.length - 1];
        }
        return "";
    }
}
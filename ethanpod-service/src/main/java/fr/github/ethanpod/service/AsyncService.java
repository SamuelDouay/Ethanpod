package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AsyncService {
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final Logger logger = LogManager.getLogger(AsyncService.class);
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingRequests;
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final int timeoutSeconds;
    private final String serviceId;
    private final MessageRouter messageRouter;

    protected AsyncService(String serviceId) {
        this(serviceId, DEFAULT_TIMEOUT_SECONDS);
    }

    protected AsyncService(String serviceId, int timeoutSeconds) {
        this.serviceId = serviceId;
        this.timeoutSeconds = timeoutSeconds;
        this.messageRouter = MessageRouter.getInstance();
        this.pendingRequests = new ConcurrentHashMap<>();
    }

    public void initialize() {
        logger.info("Initialisation du service {}", serviceId);
        active.set(true);
    }

    private boolean routeMessage(ThreadMessage message) {
        return this.messageRouter.routeMessage(message);
    }

    public void refreshData() {
        sendRequestToLogic("REFRESH_DATA", null);
    }

    public void stop() {
        logger.info("Arrêt du service {}", serviceId);
        active.set(false);
        pendingRequests.clear();
    }


    private String generateRequestId() {
        return "[" + serviceId + "]" + UUID.randomUUID();
    }

    private void sendRequestToLogic(String request, String requestId) {
        ThreadMessage message = new ThreadMessage(request, "ViewThread", "LogicThread",
                MessageType.REQUEST, null, requestId);

        logger.info("🟢 Service: Envoi message - De: {}, Pour: {}, Type: {}, Contenu: {}, ID: {}",
                message.getSender(), message.getReceiver(), message.getType(),
                message.getContent(), message.getRequestId());

        boolean success = routeMessage(message);
        if (success) {
            logger.info("🟢 Service: Message routé avec succès vers LogicThread");
        } else {
            logger.error("🔴 Service: Échec du routage du message vers LogicThread");
        }
    }

    public void handleResponse(ThreadMessage message) {
        String requestId = message.getRequestId();
        logger.info("🟢 Service: Réception réponse pour ID: {}, Type: {}",
                requestId, message.getType());

        if (requestId == null) {
            logger.warn("🟡 Service: Message sans requestId reçu");
            return;
        }

        CompletableFuture<Object> future = (CompletableFuture<Object>) pendingRequests.remove(requestId);
        if (future == null) {
            logger.warn("🟡 Service: Aucun future en attente pour ID: {}", requestId);
            return;
        }

        try {
            if (message.getType() == MessageType.ERROR) {
                logger.error("🔴 Service: Erreur reçue: {}", message.getContent());
                future.completeExceptionally(new RuntimeException(message.getContent()));
            } else {
                logger.info("🟢 Service: Completion du future avec succès");
                future.complete(message.getData());
            }
        } catch (Exception e) {
            logger.error("🔴 Service: Erreur lors de la completion du future", e);
            future.completeExceptionally(e);
        }
    }

    private void futureTimeOut(CompletableFuture<?> future, String requestId) {
        logger.info("🟢 Service: Requête enregistrée, total en attente: {}", pendingRequests.size());
        future.orTimeout(this.timeoutSeconds, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    logger.error("🔴 Service: Timeout pour requête ID: {}", requestId);
                    pendingRequests.remove(requestId);
                    return null;
                });
    }

    protected <T> CompletableFuture<T> createRequestFuture(String requestType) {
        CompletableFuture<T> future = new CompletableFuture<>();
        String requestId = generateRequestId();
        logger.info("🟢 Service: Création requête {} avec ID: {}", requestType, requestId);
        pendingRequests.put(requestId, future);
        futureTimeOut(future, requestId);
        sendRequestToLogic(requestType, requestId);
        return future;
    }
}
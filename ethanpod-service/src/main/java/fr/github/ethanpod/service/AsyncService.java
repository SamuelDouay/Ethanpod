package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.MessageCategory;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.RequestType;
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
        logger.debug("Initialisation du service {}", serviceId);
        active.set(true);
    }

    public void refreshData() {
        // no content
    }

    public void stop() {
        logger.debug("Arrêt du service {}", serviceId);
        active.set(false);
        pendingRequests.clear();
    }


    private String generateRequestId() {
        return "[" + serviceId + "]" + UUID.randomUUID();
    }


    public void handleResponse(ThreadMessage message) {
        String requestId = message.id();
        logger.debug("Service: Réception réponse pour ID: {}, Type: {}",
                requestId, message.type());

        if (requestId == null) {
            logger.warn("Message sans requestId, impossible de router");
            return;
        }

        CompletableFuture<Object> future = (CompletableFuture<Object>) pendingRequests.remove(requestId);
        if (future == null) {
            logger.warn("Service: Aucun future en attente pour ID: {}", requestId);
            return;
        }

        try {
            if (message.category() == MessageCategory.ERROR) {
                logger.error("Service: Erreur reçue: {}", message.type());
                future.completeExceptionally(new RuntimeException(String.valueOf(message.type())));
            } else {
                logger.debug("Service: Completion du future avec succès");
                future.complete(message.data());
            }
        } catch (Exception e) {
            logger.error("Service: Erreur lors de la completion du future", e);
            future.completeExceptionally(e);
        }
    }

    private void futureTimeOut(CompletableFuture<?> future, String requestId) {
        logger.debug("Service: Requête enregistrée, total en attente: {}", pendingRequests.size());
        future.orTimeout(this.timeoutSeconds, TimeUnit.SECONDS)
                .exceptionally(_ -> {
                    logger.error("Service: Timeout pour requête ID: {}", requestId);
                    pendingRequests.remove(requestId);
                    return null;
                });
    }

    private <T> CompletableFuture<RequestResult<T>> createFuture(RequestType requestType, Object data) {
        CompletableFuture<T> future = new CompletableFuture<>();
        String requestId = generateRequestId();
        logger.debug("Service: Création requête {} avec ID: {}", requestType, requestId);
        pendingRequests.put(requestId, future);
        futureTimeOut(future, requestId);
        messageRouter.sendRequest(requestType, requestId, data);
        return future.thenApply(donne -> new RequestResult<>(requestId, donne));
    }

    protected <T> CompletableFuture<RequestResult<T>> createRequestFuture(RequestType request) {
        return createFuture(request, null);
    }

    protected <T> CompletableFuture<RequestResult<T>> createRequestFuture(RequestType request, Object data) {
        return createFuture(request, data);
    }

    public record RequestResult<T>(String requestId, T data) {
    }
}
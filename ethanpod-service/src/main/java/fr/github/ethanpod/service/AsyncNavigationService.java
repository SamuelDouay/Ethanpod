package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncNavigationService {
    private static final Logger logger = LogManager.getLogger(AsyncNavigationService.class);

    private final BlockingQueue<ThreadMessage> messageQueue;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingRequests;

    public AsyncNavigationService(BlockingQueue<ThreadMessage> messageQueue) {
        this.messageQueue = messageQueue;
        this.pendingRequests = new ConcurrentHashMap<>();
    }

    public CompletableFuture<List<NavigationItem>> getListAsync() {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<List<NavigationItem>> future = new CompletableFuture<>();

        // Enregistrer la requête en attente
        pendingRequests.put(requestId, future);

        // Envoyer la requête au thread de logique
        sendRequestToLogic("GET_NAVIGATION_LIST", requestId);

        return future;
    }

    public CompletableFuture<Integer> getInboxCountAsync() {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<Integer> future = new CompletableFuture<>();

        // Enregistrer la requête en attente
        pendingRequests.put(requestId, future);

        // Envoyer la requête au thread de logique
        sendRequestToLogic("GET_INBOX_COUNT", requestId);

        return future;
    }

    public void refreshData() {
        sendRequestToLogic("REFRESH_DATA", null);
    }

    private void sendRequestToLogic(String request, String requestId) {
        try {
            ThreadMessage message = new ThreadMessage(request, "ViewThread", "LogicThread",
                    MessageType.REQUEST, null, requestId);
            messageQueue.put(message);
            logger.info("🟢 Service: Requête envoyée - {}", request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Erreur lors de l'envoi de la requête", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void handleResponse(ThreadMessage message) {
        String requestId = message.getRequestId();
        if (requestId != null && pendingRequests.containsKey(requestId)) {
            CompletableFuture<Object> future = (CompletableFuture<Object>) pendingRequests.remove(requestId);

            if (message.getType() == MessageType.ERROR) {
                future.completeExceptionally(new RuntimeException(message.getContent()));
            } else {
                future.complete(message.getData());
            }
        }
    }
}
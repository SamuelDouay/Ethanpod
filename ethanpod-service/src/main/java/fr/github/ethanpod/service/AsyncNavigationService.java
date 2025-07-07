package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AsyncNavigationService implements AsyncService {
    private static final Logger logger = LogManager.getLogger(AsyncNavigationService.class);
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingRequests;
    private final MessageRouter messageRouter;

    public AsyncNavigationService() {
        this.pendingRequests = new ConcurrentHashMap<>();
        this.messageRouter = MessageRouter.getInstance();
    }

    public CompletableFuture<List<NavigationItem>> getListAsync(String sender) {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<List<NavigationItem>> future = new CompletableFuture<>();

        logger.info("🟢 Service: Création requête GET_NAVIGATION_LIST avec ID: {}", requestId);

        // Enregistrer la requête en attente
        pendingRequests.put(requestId, future);
        logger.info("🟢 Service: Requête enregistrée, total en attente: {}", pendingRequests.size());

        // Ajouter un timeout pour éviter d'attendre indéfiniment
        future.orTimeout(30, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    logger.error("🔴 Service: Timeout pour requête ID: {}", requestId);
                    pendingRequests.remove(requestId);
                    return null;
                });

        // Envoyer la requête au thread de logique
        sendRequestToLogic("GET_NAVIGATION_LIST", sender, requestId);

        return future;
    }

    public CompletableFuture<Integer> getInboxCountAsync() {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<Integer> future = new CompletableFuture<>();

        pendingRequests.put(requestId, future);
        sendRequestToLogic("GET_INBOX_COUNT", "ViewThread", requestId);

        return future;
    }

    public void refreshData() {
        sendRequestToLogic("REFRESH_DATA", "ViewThread", null);
    }

    @Override
    public void stop() {

    }

    @Override
    public String getServiceId() {
        return "";
    }

    @Override
    public boolean isActive() {
        return false;
    }

    private void sendRequestToLogic(String request, String sender, String requestId) {
        ThreadMessage message = new ThreadMessage(request, sender, "LogicThread",
                MessageType.REQUEST, null, requestId);

        logger.info("🟢 Service: Envoi message - De: {}, Pour: {}, Type: {}, Contenu: {}, ID: {}",
                message.getSender(), message.getReceiver(), message.getType(),
                message.getContent(), message.getRequestId());

        boolean success = messageRouter.routeMessage(message);
        if (success) {
            logger.info("🟢 Service: Message routé avec succès vers LogicThread");
        } else {
            logger.error("🔴 Service: Échec du routage du message vers LogicThread");
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleResponse(ThreadMessage message) {
        String requestId = message.getRequestId();
        logger.info("🟢 Service: Réception réponse pour ID: {}, Type: {}",
                requestId, message.getType());

        if (requestId != null && pendingRequests.containsKey(requestId)) {
            CompletableFuture<Object> future = (CompletableFuture<Object>) pendingRequests.remove(requestId);
            logger.info("🟢 Service: Future trouvé et retiré pour ID: {}", requestId);

            if (message.getType() == MessageType.ERROR) {
                logger.error("🔴 Service: Erreur reçue: {}", message.getContent());
                future.completeExceptionally(new RuntimeException(message.getContent()));
            } else {
                logger.info("🟢 Service: Completion du future avec succès");
                future.complete(message.getData());
            }
        } else {
            logger.warn("🟡 Service: Aucun future en attente pour ID: {}, Requêtes en attente: {}",
                    requestId, pendingRequests.keySet());
        }
    }
}
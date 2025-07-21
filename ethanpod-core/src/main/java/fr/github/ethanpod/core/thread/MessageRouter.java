package fr.github.ethanpod.core.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageRouter {
    private static final Logger logger = LogManager.getLogger(MessageRouter.class);
    private final ConcurrentHashMap<String, BlockingQueue<ThreadMessage>> threadQueues;
    private final ConcurrentHashMap<String, String> requestSenders;

    public MessageRouter() {
        this.threadQueues = new ConcurrentHashMap<>();
        this.requestSenders = new ConcurrentHashMap<>();
    }

    public static synchronized MessageRouter getInstance() {
        return Holder.INSTANCE;
    }

    public void registerThread(String threadName, BlockingQueue<ThreadMessage> queue) {
        threadQueues.put(threadName, queue);
        logger.info("📬 Thread {} enregistré avec sa queue dédiée", threadName);
    }

    public BlockingQueue<ThreadMessage> registerThread(String threadName) {
        BlockingQueue<ThreadMessage> queue = new LinkedBlockingQueue<>();
        registerThread(threadName, queue);
        return queue;
    }

    public boolean routeMessage(ThreadMessage message) {

        if (message.getType() == MessageType.REQUEST && message.getRequestId() != null) {
            requestSenders.put(message.getRequestId(), message.getSender());
            logger.debug("📬 Requête tracée - ID: {}, Expéditeur: {}",
                    message.getRequestId(), message.getSender());
        }

        if (message.getType() == MessageType.RESPONSE && message.getRequestId() != null) {
            logger.debug("📬 Réponse reroutée vers l'expéditeur original - ID: {}, Vers: {}",
                    message.getRequestId(), message.getReceiver());
            requestSenders.remove(message.getRequestId());

        }

        BlockingQueue<ThreadMessage> targetQueue = threadQueues.get(message.getReceiver());

        if (targetQueue == null) {
            logger.error("📬 Thread destinataire {} non trouvé pour le message: {}", message.getReceiver(), message);
            return false;
        }

        try {
            targetQueue.put(message);
            logger.debug("📬 Message de {} routé vers {}: {}", message.getSender(), message.getReceiver(), message.getContent());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("📬 Erreur lors du routage du message vers {}", message.getReceiver(), e);
            return false;
        }
    }

    private void sendRequest(String request, String requestId, String sender, String receiver, MessageType messageType, Object data) {
        ThreadMessage message = new ThreadMessage(request, sender, receiver,
                messageType, data, requestId);

        logger.info("🟢 Service: Envoi message - De: {}, Pour: {}, Type: {}, Contenu: {}, ID: {}",
                message.getSender(), message.getReceiver(), message.getType(),
                message.getContent(), message.getRequestId());

        boolean success = routeMessage(message);
        if (success) {
            logger.info("🟢 Service: Message routé avec succès vers {}", receiver);
        } else {
            logger.error("🔴 Service: Échec du routage du message vers {}", receiver);
        }
    }

    public void sendRequestToLogicFromView(String request, String requestId, MessageType messageType, Object data) {
        sendRequest(request, requestId, "ViewThread", "LogicThread", messageType, data);
    }

    public void sendRequestToLogicFromEvent(String request, String requestId, MessageType messageType, Object data) {
        sendRequest(request, requestId, "UIEventThread", "LogicThread", messageType, data);
    }

    public void sendRequestToViewFromLogic(String request, String requestId, MessageType messageType, Object data) {
        sendRequest(request, requestId, "LogicThread", "ViewThread", messageType, data);
    }

    public void sendRequestToViewFromEvent(String request, String requestId, MessageType messageType, Object data) {
        sendRequest(request, requestId, "UIEventThread", "ViewThread", messageType, data);
    }

    public void sendRequestToUiEventFromView(String request, String requestId, MessageType messageType, Object data) {
        sendRequest(request, requestId, "ViewThread", "UIEventThread", messageType, data);
    }

    public void sendRequestToUiEventFromLogic(String request, String requestId, MessageType messageType, Object data) {
        sendRequest(request, requestId, "LogicThread", "UIEventThread", messageType, data);
    }

    private static class Holder {
        private static final MessageRouter INSTANCE = new MessageRouter();
    }
}
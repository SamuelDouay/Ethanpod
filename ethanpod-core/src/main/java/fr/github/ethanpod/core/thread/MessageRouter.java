package fr.github.ethanpod.core.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageRouter {
    public static final String UI_EVENT_THREAD = "UIEventThread";
    public static final String VIEW_THREAD = "ViewThread";
    public static final String LOGIC_THREAD = "LogicThread";
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
        logger.debug("Thread {} enregistré avec sa queue dédiée", threadName);
    }

    public BlockingQueue<ThreadMessage> registerThread(String threadName) {
        BlockingQueue<ThreadMessage> queue = new LinkedBlockingQueue<>();
        registerThread(threadName, queue);
        return queue;
    }

    public void routeMessage(ThreadMessage message) {
        logger.debug("Service: Envoi {} de {} vers {}, ID: {}",
                message.getType(), message.getSender(), message.getReceiver(), message.getId());

        if (message.getType() == MessageCategory.REQUEST && message.getId() != null) {
            requestSenders.put(message.getId(), message.getSender());
            logger.debug("Requête tracée - ID: {}, Expéditeur: {}",
                    message.getId(), message.getSender());
        }

        if (message.getType() == MessageCategory.RESPONSE && message.getId() != null) {
            logger.debug("Réponse reroutée vers l'expéditeur original - ID: {}, Vers: {}",
                    message.getId(), message.getReceiver());
            requestSenders.remove(message.getId());

        }

        BlockingQueue<ThreadMessage> targetQueue = threadQueues.get(message.getReceiver());

        if (targetQueue == null) {
            logger.error("Thread destinataire {} non trouvé pour le message: {}", message.getReceiver(), message);
            return;
        }

        try {
            targetQueue.put(message);
            logger.debug("Message de {} routé vers {}: {}", message.getSender(), message.getReceiver(), message.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Erreur lors du routage du message vers {}", message.getReceiver(), e);
        }
    }

    public void sendRequest(RequestType requestType, String requestId, Object data) {
        ThreadMessage message = MessageBuilder.request(requestType, requestId, data);
        routeMessage(message);
    }

    public void sendResponse(String requestId, ResponseType responseType, Object data) {
        ThreadMessage message = MessageBuilder.response(requestId, responseType, data);
        routeMessage(message);
    }

    public void sendEvent(EventType eventType, String requestId, Object data) {
        ThreadMessage message = MessageBuilder.event(eventType, requestId, data);
        routeMessage(message);
    }

    public void sendNotification(String senderId, String receiverId, NotificationType notificationType) {
        ThreadMessage message = MessageBuilder.notification(senderId, receiverId, notificationType);
        routeMessage(message);
    }

    private static class Holder {
        private static final MessageRouter INSTANCE = new MessageRouter();
    }
}
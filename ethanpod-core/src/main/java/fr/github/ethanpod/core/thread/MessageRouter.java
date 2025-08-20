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
    public static final String JAVAFX_THREAD = "JavaFX Application Thread";
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
                message.type(), message.sender(), message.receiver(), message.id());

        if (message.type() == MessageCategory.REQUEST && message.id() != null) {
            requestSenders.put(message.id(), message.sender());
            logger.debug("Requête tracée - ID: {}, Expéditeur: {}",
                    message.id(), message.sender());
        }

        if (message.type() == MessageCategory.RESPONSE && message.id() != null) {
            logger.debug("Réponse reroutée vers l'expéditeur original - ID: {}, Vers: {}",
                    message.id(), message.receiver());
            requestSenders.remove(message.id());

        }

        BlockingQueue<ThreadMessage> targetQueue = threadQueues.get(message.receiver());

        if (targetQueue == null) {
            logger.error("Thread destinataire {} non trouvé pour le message: {}", message.receiver(), message);
            return;
        }

        try {
            targetQueue.put(message);
            logger.debug("Message de {} routé vers {}: {}", message.sender(), message.receiver(), message.id());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Erreur lors du routage du message vers {}", message.receiver(), e);
        }
    }

    public void sendRequest(RequestType requestType, String requestId, Object data) {
        routeMessage(MessageBuilder.request(requestType, requestId, data));
    }

    public void sendResponse(String requestId, ResponseType responseType, Object data) {
        routeMessage(MessageBuilder.response(requestId, responseType, data));
    }

    public void sendEvent(EventType eventType, String requestId, Object data) {
        routeMessage(MessageBuilder.event(eventType, requestId, data));
    }

    public void sendNotification(String senderId, String receiverId, NotificationType notificationType) {
        routeMessage(MessageBuilder.notification(senderId, receiverId, notificationType));
    }

    public void userRequest(UserRequestType type, String requestId, Object data) {
        routeMessage(MessageBuilder.userRequest(type, requestId, data));
    }

    private static class Holder {
        private static final MessageRouter INSTANCE = new MessageRouter();
    }
}
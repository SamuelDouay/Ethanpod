package fr.github.ethanpod.core.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageRouter {
    private static final Logger logger = LogManager.getLogger(MessageRouter.class);
    private static MessageRouter instance;
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
        logger.info("📬 Thread '{}' enregistré avec sa queue dédiée", threadName);
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

        String receiver = message.getReceiver();
        if (message.getType() == MessageType.RESPONSE && message.getRequestId() != null) {
            String originalSender = requestSenders.get(message.getRequestId());
            if (originalSender != null) {
                receiver = originalSender;
                logger.debug("📬 Réponse reroutée vers l'expéditeur original - ID: {}, Vers: {}",
                        message.getRequestId(), receiver);
                requestSenders.remove(message.getRequestId());
            }
        }

        BlockingQueue<ThreadMessage> targetQueue = threadQueues.get(receiver);

        if (targetQueue == null) {
            logger.error("📬 Thread destinataire '{}' non trouvé pour le message: {}", receiver, message);
            return false;
        }

        try {
            targetQueue.put(message);
            logger.debug("📬 Message de {} routé vers '{}': {}", message.getSender(), receiver, message.getContent());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("📬 Erreur lors du routage du message vers '{}'", receiver, e);
            return false;
        }
    }

    public BlockingQueue<ThreadMessage> getQueueForThread(String threadName) {
        return threadQueues.get(threadName);
    }

    private static class Holder {
        private static final MessageRouter INSTANCE = new MessageRouter();
    }
}
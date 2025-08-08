package fr.github.ethanpod.core.exception.thread;

import fr.github.ethanpod.core.exception.EthanpodException;

public class ThreadCommunicationException extends EthanpodException {

    public ThreadCommunicationException(String message) {
        super("THREAD_COMM_ERROR", message);
    }

    public ThreadCommunicationException(String message, Throwable cause) {
        super("THREAD_COMM_ERROR", message, cause);
    }

    public static ThreadCommunicationException messageRoutingFailed(String from, String to, String messageId) {
        return new ThreadCommunicationException(
                String.format("Échec du routage du message %s de %s vers %s", messageId, from, to)
        );
    }

    public static ThreadCommunicationException queueFull(String threadName) {
        return new ThreadCommunicationException(
                "Queue pleine pour le thread: " + threadName
        );
    }

    public static ThreadCommunicationException messageServiceUnknow(String service) {
        return new ThreadCommunicationException(
                "Service inconnu: " + service
        );
    }

    public static ThreadCommunicationException messageErrorReceive(String eventType) {
        return new ThreadCommunicationException(
                "Message erreur reçue: " + eventType
        );
    }
}
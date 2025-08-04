package fr.github.ethanpod.core.thread;

import java.time.LocalDateTime;

public class ThreadMessage {
    private final String id;
    private final String sender;
    private final String receiver;
    private final MessageCategory category;
    private final Enum<?> type;
    private final Object data;
    private final LocalDateTime timestamp;

    public ThreadMessage(String id, String sender, String receiver,
                         MessageCategory category, Enum<?> type, Object data) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.category = category;
        this.type = type;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public MessageCategory getCategory() {
        return category;
    }

    public Enum<?> getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return String.format("%s -> %s (%s): %s avec l'ID %s",
                sender, receiver, category, type, id);
    }
}

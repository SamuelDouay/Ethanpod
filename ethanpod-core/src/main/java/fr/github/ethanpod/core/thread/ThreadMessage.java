package fr.github.ethanpod.core.thread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadMessage {
    private final String content;
    private final String sender;
    private final String receiver;
    private final MessageType type;
    private final LocalDateTime timestamp;
    private final Object data;
    private final String requestId;

    public ThreadMessage(String content, String sender, String receiver, MessageType type, Object data, String requestId) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.data = data;
        this.requestId = requestId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public MessageType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s (%s): %s",
                timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")),
                sender, receiver, type, content);
    }
}

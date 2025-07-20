package fr.github.ethanpod.view.controller.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class UIEvent {
    private final String eventId;
    private final LocalDateTime timestamp;
    private final String source;

    protected UIEvent(String source) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.source = source;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public abstract String getEventType();
}

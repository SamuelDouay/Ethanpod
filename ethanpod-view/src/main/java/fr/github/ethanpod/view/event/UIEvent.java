package fr.github.ethanpod.view.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class UIEvent {
    private final String eventId;
    private final LocalDateTime timestamp;
    private final String source;
    private final String eventType;

    protected UIEvent(String source, String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.source = source;
        this.eventType = eventType;
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

    public String getEventType() {
        return eventType;
    }
}

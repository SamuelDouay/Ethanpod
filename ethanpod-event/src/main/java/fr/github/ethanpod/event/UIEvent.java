package fr.github.ethanpod.event;

import fr.github.ethanpod.core.thread.EventType;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class UIEvent {
    public final EventType eventType;
    private final String eventId;
    private final LocalDateTime timestamp;
    private final String source;

    protected UIEvent(String source, EventType eventType) {
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

    public EventType getEventType() {
        return eventType;
    }
}

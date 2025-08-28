package fr.github.ethanpod.event;

import fr.github.ethanpod.core.thread.EventType;

public class InboxCountUpdatedEvent extends UIEvent {

    public InboxCountUpdatedEvent(String source, Integer count) {
        super(source, EventType.INBOX_COUNT_UPDATED);
        this.count = count;
    }
}

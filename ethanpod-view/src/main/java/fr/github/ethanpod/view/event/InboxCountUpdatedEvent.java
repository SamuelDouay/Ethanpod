package fr.github.ethanpod.view.event;

public class InboxCountUpdatedEvent extends UIEvent {
    public static final String EVENT_TYPE = "INBOX_COUNT_UPDATED";
    private final Integer count;

    public InboxCountUpdatedEvent(String source, Integer count) {
        super(source, EVENT_TYPE);
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }
}

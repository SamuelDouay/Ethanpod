package fr.github.ethanpod.view.thread.callback;

public class InboxCountUpdatedEvent extends UIEvent {
    public static final String EVENT_TYPE = "INBOX_COUNT_UPDATED";
    private final Integer count;

    public InboxCountUpdatedEvent(String source, Integer count) {
        super(source);
        this.count = count;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public Integer getCount() {
        return count;
    }
}

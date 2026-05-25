package fr.github.ethanpod.event.updated;

public class InboxCountUpdated {
    private final int count;

    public InboxCountUpdated(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }
}


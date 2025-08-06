package fr.github.ethanpod.core.thread;

public record ThreadMessage(String id, String sender, String receiver, MessageCategory category, Enum<?> type,
                            Object data) {

    @Override
    public String toString() {
        return String.format("%s -> %s (%s): %s avec l'ID %s",
                sender, receiver, category, type, id);
    }
}

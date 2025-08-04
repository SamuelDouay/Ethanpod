package fr.github.ethanpod.core.thread;

import static fr.github.ethanpod.core.thread.MessageRouter.*;

public class MessageBuilder {

    private MessageBuilder() {
        // no param
    }

    public static ThreadMessage request(RequestType type, String requestId, Object data) {
        return new ThreadMessage(requestId, VIEW_THREAD, LOGIC_THREAD, MessageCategory.REQUEST, type, data);
    }

    public static ThreadMessage response(String requestId, ResponseType type, Object data) {
        return new ThreadMessage(requestId, LOGIC_THREAD, VIEW_THREAD, MessageCategory.RESPONSE, type, data);
    }

    public static ThreadMessage event(EventType type, String requestId, Object data) {
        return new ThreadMessage(requestId, VIEW_THREAD, UI_EVENT_THREAD, MessageCategory.EVENT, type, data);
    }

    public static ThreadMessage notification(String senderId, String receiverId, NotificationType type) {
        String id = generateId(type.name());
        return new ThreadMessage(id, senderId, receiverId, MessageCategory.NOTIFICATION, type, null);
    }

    private static String generateId(String typeName) {
        return String.format("(%s)%s", "NOT", typeName);
    }
}

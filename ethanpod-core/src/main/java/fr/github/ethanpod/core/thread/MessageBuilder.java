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
        return new ThreadMessage(type.name(), senderId, receiverId, MessageCategory.NOTIFICATION, type, null);
    }

    public static ThreadMessage userRequest(UserRequestType type, String requestId, Object data) {
        return new ThreadMessage(requestId, JAVAFX_THREAD, VIEW_THREAD, MessageCategory.USER_REQUEST, type, data);
    }
}

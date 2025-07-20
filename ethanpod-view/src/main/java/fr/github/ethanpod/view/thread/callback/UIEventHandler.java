package fr.github.ethanpod.view.thread.callback;

public interface UIEventHandler<T extends UIEvent> {
    void handleEvent(T event);

    boolean canHandle(Class<? extends UIEvent> eventType);
}

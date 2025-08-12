package fr.github.ethanpod.event;

public interface UIEventHandler<T extends UIEvent> {
    void handleEvent(T event);
}

package fr.github.ethanpod.view.event;

public interface UIEventHandler<T extends UIEvent> {
    void handleEvent(T event);
}

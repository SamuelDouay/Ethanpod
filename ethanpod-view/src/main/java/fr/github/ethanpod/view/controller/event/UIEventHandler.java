package fr.github.ethanpod.view.controller.event;

public interface UIEventHandler<T extends UIEvent> {
    void handleEvent(T event);
}

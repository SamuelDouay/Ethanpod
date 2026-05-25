package fr.github.ethanpod.logic.handler;

import fr.github.ethanpod.event.GlobalEventBus;

public abstract class BaseRequestHandler {
    protected BaseRequestHandler() {
        GlobalEventBus.getInstance().register(this);
    }

    protected void postEvent(Object event) {
        GlobalEventBus.getInstance().post(event);
    }
}
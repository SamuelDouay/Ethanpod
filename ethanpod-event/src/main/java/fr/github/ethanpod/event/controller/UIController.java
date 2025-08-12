package fr.github.ethanpod.event.controller;

import fr.github.ethanpod.event.UIEvent;
import fr.github.ethanpod.event.UIEventManager;
import javafx.application.Platform;

import java.util.function.Supplier;

public class UIController {
    protected final String controllerName;
    private final UIEventManager eventManager = new UIEventManager();

    protected UIController() {
        this.controllerName = this.getClass().getSimpleName().replace("UIController", "Controller");
    }

    protected void publishEvent(Supplier<UIEvent> eventSupplier) {
        Platform.runLater(() -> {
            UIEvent event = eventSupplier.get();
            eventManager.publishEvent(event);
        });
    }

    protected boolean isValidData(Object data) {
        return data != null;
    }

    protected boolean isValidList(java.util.List<?> list) {
        return list != null && !list.isEmpty();
    }
}

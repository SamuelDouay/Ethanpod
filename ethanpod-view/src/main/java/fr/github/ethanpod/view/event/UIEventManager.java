package fr.github.ethanpod.view.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIEventManager {
    private static final Logger logger = LogManager.getLogger(UIEventManager.class);
    private final Map<String, List<UIEventHandler<? extends UIEvent>>> handlers;

    private UIEventManager() {
        this.handlers = new ConcurrentHashMap<>();
    }

    public static UIEventManager getInstance() {
        return UIEventManager.Holder.INSTANCE;
    }

    public <T extends UIEvent> void registerHandler(String eventType, UIEventHandler<T> handler) {
        if (eventType == null || handler == null) {
            throw new IllegalArgumentException("Event type and handler cannot be null");
        }
        handlers.computeIfAbsent(eventType, _ -> new CopyOnWriteArrayList<>()).add(handler);
        logger.debug("Handler enregistré pour le type d'événement: {}", eventType);
    }

    @SuppressWarnings("unchecked")
    public void publishEvent(UIEvent event) {
        String eventType = event.getEventType();

        List<UIEventHandler<? extends UIEvent>> eventHandlers = handlers.get(eventType);
        if (eventHandlers == null || eventHandlers.isEmpty()) {
            logger.warn("Aucun handler trouvé pour l'événement: {}", eventType);
            return;
        }

        logger.debug("Publication de l'événement {} vers {} handler(s)",
                eventType, eventHandlers.size());

        for (UIEventHandler<? extends UIEvent> handler : eventHandlers) {
            try {
                // Cast sécurisé car on vérifie le type lors de l'enregistrement
                ((UIEventHandler<UIEvent>) handler).handleEvent(event);
            } catch (Exception e) {
                logger.error("Erreur lors du traitement de l'événement {} par le handler {}: {}",
                        eventType, handler.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
    }

    private static class Holder {
        private static final UIEventManager INSTANCE = new UIEventManager();
    }
}

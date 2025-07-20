package fr.github.ethanpod.view.thread.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIEventManager {
    private static final Logger logger = LogManager.getLogger(UIEventManager.class);

    // Map: Type d'événement -> Liste des handlers
    private final Map<String, List<UIEventHandler<? extends UIEvent>>> handlers;

    // Pour les statistiques et le debugging
    private final Map<String, Integer> eventStats;

    private UIEventManager() {
        this.handlers = new ConcurrentHashMap<>();
        this.eventStats = new ConcurrentHashMap<>();
    }

    public static UIEventManager getInstance() {
        return UIEventManager.Holder.INSTANCE;
    }

    public <T extends UIEvent> void registerHandler(String eventType, UIEventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, _ -> new CopyOnWriteArrayList<>()).add(handler);
        logger.info("Handler enregistré pour le type d'événement: {}", eventType);
    }

    public void unregisterHandler(String eventType, UIEventHandler<?> handler) {
        List<UIEventHandler<? extends UIEvent>> eventHandlers = handlers.get(eventType);
        if (eventHandlers != null) {
            eventHandlers.remove(handler);
            if (eventHandlers.isEmpty()) {
                handlers.remove(eventType);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void publishEvent(UIEvent event) {
        String eventType = event.getEventType();

        // Mise à jour des stats
        eventStats.merge(eventType, 1, Integer::sum);

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

    public Map<String, Integer> getEventStats() {
        return new HashMap<>(eventStats);
    }

    public void clearStats() {
        eventStats.clear();
    }

    public Map<String, Integer> getHandlerCounts() {
        Map<String, Integer> counts = new HashMap<>();
        handlers.forEach((type, handlerList) -> counts.put(type, handlerList.size()));
        return counts;
    }

    private static class Holder {
        private static final UIEventManager INSTANCE = new UIEventManager();
    }
}

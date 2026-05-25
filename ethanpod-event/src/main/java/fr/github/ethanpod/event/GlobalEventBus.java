package fr.github.ethanpod.event;

import com.google.common.eventbus.AsyncEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GlobalEventBus {
    private static final Logger logger = LogManager.getLogger(GlobalEventBus.class);

    // Thread unique pour sérialiser les traitements (remplace UIEventThread)
    private static final ExecutorService uiEventExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "ethanpod-ui-event-thread");
        t.setDaemon(true);
        return t;
    });

    private static final AsyncEventBus INSTANCE = new AsyncEventBus(
            "ethanpod-global-bus",
            uiEventExecutor
            /*(exception, context) -> {
                logger.error("Erreur dans le subscriber {} (événement {})",
                        context.getSubscriber().getClass().getSimpleName(),
                        context.getEvent().getClass().getSimpleName(),
                        exception);
            }*/
    );

    private GlobalEventBus() {
    }

    public static AsyncEventBus getInstance() {
        return INSTANCE;
    }
}
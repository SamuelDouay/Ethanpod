package fr.github.ethanpod.event;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GlobalEventBus {

    private static final ExecutorService uiEventExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "ethanpod-ui-event-thread");
        t.setDaemon(true);
        return t;
    });

    private static final AsyncEventBus INSTANCE = new AsyncEventBus(
            "ethanpod-global-bus",
            uiEventExecutor
    );

    private GlobalEventBus() {
    }

    public static AsyncEventBus getInstance() {
        return INSTANCE;
    }
}
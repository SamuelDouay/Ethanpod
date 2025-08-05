package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.service.AsyncService;
import fr.github.ethanpod.service.AsyncServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class Controller {
    protected static final Logger logger = LogManager.getLogger(Controller.class);
    protected static final ExecutorService UI_EXECUTOR = Executors.newCachedThreadPool(
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("UI-Loader-" + threadNumber.getAndIncrement());
                    thread.setDaemon(false);
                    return thread;
                }
            }
    );
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    protected AsyncServiceManager asyncServiceManager;

    protected Controller(AsyncServiceManager asyncServiceManager) {
        this.asyncServiceManager = asyncServiceManager;
    }


    protected void logRequestTime(String result, long executionTime) {
        logger.debug("RESQUEST : {} {}ms", result, executionTime);
    }

    protected <T> void executeAsyncOperation(
            String operationName,
            Supplier<CompletableFuture<AsyncService.RequestResult<T>>> asyncOperation,
            EventType eventType,
            String errorMessage) {

        long startTime = System.currentTimeMillis();
        logger.info(operationName);

        asyncOperation.get()
                .thenAccept(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logRequestTime(result.requestId(), executionTime);
                    messageRouter.sendEvent(eventType, result.requestId(), result.data());
                })
                .exceptionally(throwable -> {
                    logger.error(errorMessage, throwable);
                    return null;
                });
    }

    abstract void initializeUI();
}

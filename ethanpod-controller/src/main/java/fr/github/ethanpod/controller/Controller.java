package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.service.AsyncService;
import fr.github.ethanpod.service.AsyncServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class Controller {
    protected static final Logger logger = LogManager.getLogger(Controller.class);
    protected final MessageRouter messageRouter = MessageRouter.getInstance();
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

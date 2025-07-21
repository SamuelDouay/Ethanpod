package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public abstract class DataService {
    private static final Logger logger = LogManager.getLogger(DataService.class);
    protected final ExecutorService executor;
    private final MessageRouter messageRouter = MessageRouter.getInstance();

    protected DataService(ExecutorService executor) {
        this.executor = executor;
    }

    abstract void refreshData();

    protected <T> void executeAsync(String requestId, String successMessageType,
                                    Supplier<T> operation, String operationName) {
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        T result = operation.get();
                        logger.info("🔵 {} completed successfully", operationName);
                        return result;
                    } catch (Exception e) {
                        throw new RuntimeException("Error in " + operationName, e);
                    }
                }, executor)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Erreur {}", operationName, throwable);
                        messageRouter.sendRequestToViewFromLogic("ERROR", requestId, MessageType.ERROR, throwable.getMessage());
                    } else {
                        messageRouter.sendRequestToViewFromLogic(successMessageType, requestId, MessageType.RESPONSE, result);
                    }
                });
    }
}

package fr.github.ethanpod.logic.services;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class LogicDataService {
    private static final Logger logger = LogManager.getLogger(LogicDataService.class);
    private final NavigationDao navigationDao;
    private final ExecutorService executor;
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    private final AtomicInteger requestCounter = new AtomicInteger(0);

    public LogicDataService(NavigationDao navigationDao, ExecutorService executor) {
        this.navigationDao = navigationDao;
        this.executor = executor;
    }

    public void getNavigationListAsync(String requestId) {
        int opId = requestCounter.incrementAndGet();

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        logger.info("🔵 Logique [{}]: Récupération liste navigation", opId);
                        List<NavigationItem> navigationList = navigationDao.getList();
                        logger.info("🔵 Logique [{}]: {} éléments récupérés", opId, navigationList.size());
                        return navigationList;
                    } catch (Exception e) {
                        throw new RuntimeException("Error getting navigation list", e);
                    }
                }, executor)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Erreur navigation list", throwable);
                        messageRouter.sendRequestToView("ERROR", requestId, MessageType.ERROR, throwable.getMessage());
                    } else {
                        messageRouter.sendRequestToView("NAVIGATION_LIST_RESULT", requestId, MessageType.RESPONSE, result);
                    }
                });
    }

    public void getInboxCountAsync(String requestId) {
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        int count = navigationDao.getNumberOfInbox();
                        logger.info("🔵 Inbox count: {}", count);
                        return count;
                    } catch (Exception e) {
                        throw new RuntimeException("Error getting inbox count", e);
                    }
                }, executor)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        messageRouter.sendRequestToView("ERROR", requestId, MessageType.ERROR, throwable.getMessage());
                    } else {
                        messageRouter.sendRequestToView("INBOX_COUNT_RESPONSE", requestId, MessageType.RESPONSE, result);
                    }
                });
    }

    public void refreshNavigationDataAsync() {
        logger.info("🔵 Refresh automatique des données");

        CompletableFuture<List<NavigationItem>> navFuture =
                CompletableFuture.supplyAsync(navigationDao::getList, executor);

        CompletableFuture<Integer> inboxFuture =
                CompletableFuture.supplyAsync(navigationDao::getNumberOfInbox, executor);

        CompletableFuture.allOf(navFuture, inboxFuture)
                .thenRun(() -> {
                    try {
                        List<NavigationItem> updatedList = navFuture.get();
                        int inboxCount = inboxFuture.get();
                        messageRouter.sendRequestToView("DATA_REFRESHED", null, MessageType.DATA_UPDATE, updatedList);
                        messageRouter.sendRequestToView("INBOX_UPDATED", null, MessageType.DATA_UPDATE, inboxCount);
                    } catch (Exception e) {
                        logger.error("Erreur refresh données", e);
                    }
                });
    }
}
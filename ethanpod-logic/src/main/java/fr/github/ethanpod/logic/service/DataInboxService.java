package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.logic.sql.dao.InboxDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class DataInboxService extends DataService {
    private static final Logger logger = LogManager.getLogger(DataInboxService.class);
    private final InboxDao inboxDao;
    private final MessageRouter messageRouter = MessageRouter.getInstance();

    public DataInboxService(ExecutorService executor) {
        super(executor);
        this.inboxDao = new InboxDao();
    }

    @Override
    void refreshData() {

    }

    public void getInboxCountAsync(String requestId) {
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        int count = inboxDao.getNumberOfInbox();
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
}

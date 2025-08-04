package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.InboxDao;

import java.util.concurrent.ExecutorService;

public class DataInboxService extends DataService {
    private final InboxDao inboxDao;

    public DataInboxService(ExecutorService executor) {
        super(executor);
        this.inboxDao = new InboxDao();
    }

    @Override
    void refreshData() {
        // no
    }

    public void getInboxCountAsync(String requestId) {
        executeAsync(requestId, ResponseType.INBOX_COUNT_RESULT,
                inboxDao::getNumberOfInbox,
                "getting inbox count");
    }

    public void getTop8InInbox(String requestId) {
        executeAsync(requestId, ResponseType.INBOX_TOP8_RESULT,
                inboxDao::getTop8InInbox,
                "getting inbox top 8");
    }
}

package fr.github.ethanpod.logic.service;

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
        executeAsync(requestId, "INBOX_COUNT_RESPONSE",
                inboxDao::getNumberOfInbox,
                "getting inbox count");
    }

    public void getTop8InInbox(String requestId) {
        executeAsync(requestId, "INBOX_TOP8_RESPONSE",
                inboxDao::getTop8InInbox,
                "getting inbox top 8");
    }
}

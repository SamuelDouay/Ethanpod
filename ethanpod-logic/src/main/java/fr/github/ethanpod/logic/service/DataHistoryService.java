package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.HistoryDao;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.concurrent.ExecutorService;

public class DataHistoryService extends DataService {
    private final HistoryDao historyDao;

    public DataHistoryService(ExecutorService executor, DatabaseManager databaseManager) {
        super(executor);
        this.historyDao = new HistoryDao(databaseManager);
    }

    @Override
    void refreshData() {
        // no
    }

    public void getAllInHistory(String requestId, UserDataRequest userDataRequest) {
        executeAsync(requestId, ResponseType.HISTORY_ALL_RESULT,
                () -> historyDao.getAllInHistory(userDataRequest),
                "getting all in history");
    }
}

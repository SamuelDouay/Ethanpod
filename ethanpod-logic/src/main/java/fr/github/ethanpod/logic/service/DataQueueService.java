package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.QueueDao;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.concurrent.ExecutorService;

public class DataQueueService extends DataService {
    private final QueueDao queueDao;

    public DataQueueService(ExecutorService executor, DatabaseManager databaseManager) {
        super(executor);
        this.queueDao = new QueueDao(databaseManager);
    }

    @Override
    void refreshData() {
        // no
    }

    public void getQueueTop8(String requestId) {
        executeAsync(requestId, ResponseType.QUEUE_TOP8_RESULT,
                queueDao::getTop8InQueue,
                "getting top 8 queue");
    }

    public void getAllInQueue(String requestId, UserDataRequest userDataRequest) {
        executeAsync(requestId, ResponseType.QUEUE_ALL_RESULT,
                () -> queueDao.getAllInQueue(userDataRequest),
                "getting all in queue");
    }
}

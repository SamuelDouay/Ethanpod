package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.SurpriseDao;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.concurrent.ExecutorService;

public class DataSurpriseService extends DataService {
    private final SurpriseDao surpriseDao;

    public DataSurpriseService(ExecutorService executor, DatabaseManager databaseManager) {
        super(executor);
        this.surpriseDao = new SurpriseDao(databaseManager);
    }

    @Override
    void refreshData() {
        // no
    }

    public void getSurpriseListAsync(String requestId) {
        executeAsync(requestId, ResponseType.SURPRISE_ALL_RESULT,
                surpriseDao::getList,
                "getting surprise list");
    }
}

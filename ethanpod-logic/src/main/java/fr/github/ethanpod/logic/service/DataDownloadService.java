package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.DownloadDao;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.concurrent.ExecutorService;

public class DataDownloadService extends DataService {
    private final DownloadDao downloadDao;

    public DataDownloadService(ExecutorService executor, DatabaseManager databaseManager) {
        super(executor);
        this.downloadDao = new DownloadDao(databaseManager);
    }

    @Override
    void refreshData() {
        // no
    }

    public void getQueueTop8(String requestId) {
        executeAsync(requestId, ResponseType.DOWNLOAD_TOP8_RESULT,
                downloadDao::getTop8Download,
                "getting top 8 download");
    }
}


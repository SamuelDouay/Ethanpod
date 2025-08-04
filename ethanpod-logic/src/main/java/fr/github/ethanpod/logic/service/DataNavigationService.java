package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;

import java.util.concurrent.ExecutorService;

public class DataNavigationService extends DataService {
    private final NavigationDao navigationDao;

    public DataNavigationService(ExecutorService executor) {
        super(executor);
        this.navigationDao = new NavigationDao();
    }

    @Override
    void refreshData() {
        // no
    }

    public void getNavigationListAsync(String requestId) {
        executeAsync(requestId, ResponseType.NAVIGATION_LIST_RESULT,
                navigationDao::getList,
                "getting navigation list");
    }
}

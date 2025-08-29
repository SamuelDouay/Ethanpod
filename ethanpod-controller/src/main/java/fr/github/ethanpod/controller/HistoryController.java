package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class HistoryController extends Controller {
    public HistoryController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadHistoryAll(UserDataRequest userDataRequest) {
        executeAsyncOperation(
                "Chargement du all in history",
                () -> asyncServiceManager.getHistoryService().getHistoryAll(userDataRequest),
                EventType.HISTORY_ALL_UPDATED,
                "Erreur lors du chargement du all in history"
        );
    }
}

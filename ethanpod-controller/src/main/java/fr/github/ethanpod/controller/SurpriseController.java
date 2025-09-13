package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class SurpriseController extends Controller {

    public SurpriseController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadSurpriseData(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement des données de navigation",
                () -> asyncServiceManager.getSurpriseService().getListAsync(message.id()),
                EventType.SURPRISE_ALL_UPDATED,
                "Erreur lors du chargement de la navigation"
        );
    }
}

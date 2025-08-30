package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class NavigationController extends Controller {

    public NavigationController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadNavigationData(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement des données de navigation",
                () -> asyncServiceManager.getNavigationService().getListAsync(message.id()),
                EventType.NAVIGATION_UPDATED,
                "Erreur lors du chargement de la navigation"
        );
    }
}

package fr.github.ethanpod.service.thread;

import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class NavigationController extends Controller {

    public NavigationController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadNavigationData() {
        logger.info("🟢 Chargement des données de navigation");

        asyncServiceManager.getNavigationService().getListAsync()
                .thenAccept(navigationList -> {
                    logger.info("🟢 {} éléments de navigation reçus", navigationList.size());
                    messageRouter.sendRequestToUiEventFromView("NAVIGATION_UPDATED", null, MessageType.EVENT, navigationList);
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement de la navigation : {}", throwable.getMessage());
                    return null;
                });

    }

    @Override
    void initializeUI() {
        loadNavigationData();
    }
}

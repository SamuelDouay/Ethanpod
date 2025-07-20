package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.controller.ui.NavigationUIController;

public class NavigationController extends Controller {
    private final NavigationUIController controller;

    public NavigationController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
        this.controller = new NavigationUIController();
    }

    public void loadNavigationData() {
        logger.info("🟢 Chargement des données de navigation");

        asyncServiceManager.getNavigationService().getListAsync()
                .thenAccept(navigationList -> {
                    logger.info("🟢 {} éléments de navigation reçus", navigationList.size());
                    controller.updateNavigationUI(navigationList);
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

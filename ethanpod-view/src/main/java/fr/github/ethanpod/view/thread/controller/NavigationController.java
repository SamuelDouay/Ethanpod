package fr.github.ethanpod.view.thread.controller;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.service.AsyncServiceManager;
import javafx.application.Platform;

import java.util.List;

public class NavigationController extends Controller {

    public NavigationController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }

    public void loadNavigationData() {
        logger.info("🟢 Chargement des données de navigation");

        asyncServiceManager.getNavigationService().getListAsync()
                .thenAccept(navigationList -> {
                    logger.info("🟢 {} éléments de navigation reçus", navigationList.size());
                    updateNavigationUI(navigationList);
                })
                .exceptionally(throwable -> {
                    logger.error("🔴 Erreur lors du chargement de la navigation : {}", throwable.getMessage());
                    return null;
                });
    }

    public void updateNavigationUI(List<NavigationItem> navigationList) {
        Platform.runLater(() -> doUpdateNavigationUI(navigationList));
    }

    private void doUpdateNavigationUI(List<NavigationItem> navigationList) {
        try {
            if (uiUpdateCallback != null) {
                logger.info("🟢 Interface mise à jour avec {} éléments", navigationList.size());
                this.uiUpdateCallback.updateNavigationList(navigationList);
            } else {
                logger.warn("NavigationContainer n'est pas encore initialisé");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'interface", e);
        }
    }

    @Override
    void initializeUI() {
        loadNavigationData();
    }
}

package fr.github.ethanpod.view.thread.controller;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.service.AsyncServiceManager;
import fr.github.ethanpod.view.thread.callback.NavigationUpdatedEvent;
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
        Platform.runLater(() -> {
            // Publier l'événement de mise à jour de navigation
            NavigationUpdatedEvent event = new NavigationUpdatedEvent(
                    "NavigationController", navigationList
            );
            eventManager.publishEvent(event);
        });
    }

    @Override
    void initializeUI() {
        loadNavigationData();
    }
}

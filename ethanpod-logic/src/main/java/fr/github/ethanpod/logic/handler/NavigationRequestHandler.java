package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetNavigationRequest;
import fr.github.ethanpod.event.updated.NavigationAllUpdated;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NavigationRequestHandler {
    private static final Logger logger = LogManager.getLogger(NavigationRequestHandler.class);
    private final NavigationDao navigationDao; // ou tout autre DAO adapté

    public NavigationRequestHandler(NavigationDao navigationDao) {
        this.navigationDao = navigationDao;
        GlobalEventBus.getInstance().register(this);
        logger.debug("NavigationRequestHandler enregistré");
    }

    @Subscribe
    public void onGetNavigationList(GetNavigationRequest request) {
        logger.debug("Réception d'une demande de liste de navigation");
        try {
            List<NavigationItem> items = navigationDao.getList();
            GlobalEventBus.getInstance().post(new NavigationAllUpdated(items));
            logger.debug("Liste de navigation envoyée ({} podcasts)", items.size());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste de navigation", e);
            // Optionnel : poster un événement d’erreur
        }
    }
}
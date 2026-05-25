package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetNavigationRequest;
import fr.github.ethanpod.event.updated.SurpriseAllUpdated;
import fr.github.ethanpod.logic.sql.dao.SurpriseDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SurpriseRequestHandler {
    private static final Logger logger = LogManager.getLogger(SurpriseRequestHandler.class);
    private final SurpriseDao surpriseDao; // ou tout autre DAO adapté

    public SurpriseRequestHandler(SurpriseDao surpriseDao) {
        this.surpriseDao = surpriseDao;
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetNavigationList(GetNavigationRequest request) {

        try {
            List<SurpriseItem> items = surpriseDao.getList();
            GlobalEventBus.getInstance().post(new SurpriseAllUpdated(items));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // Optionnel : poster un événement d’erreur
        }
    }
}
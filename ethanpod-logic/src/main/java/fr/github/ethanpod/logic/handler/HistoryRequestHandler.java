package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetHistoryAllRequest;
import fr.github.ethanpod.event.updated.HistoryAllUpdated;
import fr.github.ethanpod.logic.sql.dao.HistoryDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class HistoryRequestHandler {
    private static final Logger logger = LogManager.getLogger(HistoryRequestHandler.class);
    private final HistoryDao historyDao;

    public HistoryRequestHandler(HistoryDao historyDao) {
        this.historyDao = historyDao;
        // S'enregistre automatiquement sur le bus global
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetHistoryAll(GetHistoryAllRequest request) {
        try {
            List<EpisodeItem> list = historyDao.getAllInHistory(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new HistoryAllUpdated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }
}

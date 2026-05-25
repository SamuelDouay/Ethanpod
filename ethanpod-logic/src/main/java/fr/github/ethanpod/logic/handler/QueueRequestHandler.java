package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetDownloadTop8Request;
import fr.github.ethanpod.event.request.GetQueueAllRequest;
import fr.github.ethanpod.event.updated.DownloadAllUpdated;
import fr.github.ethanpod.event.updated.QueueTop8Updated;
import fr.github.ethanpod.logic.sql.dao.QueueDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class QueueRequestHandler {
    private static final Logger logger = LogManager.getLogger(QueueRequestHandler.class);
    private final QueueDao queueDao;

    public QueueRequestHandler(QueueDao queueDao) {
        this.queueDao = queueDao;
        // S'enregistre automatiquement sur le bus global
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetQueueTop8(GetDownloadTop8Request request) {
        try {
            List<EpisodeItem> list = queueDao.getTop8InQueue();
            GlobalEventBus.getInstance().post(new QueueTop8Updated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetQueueAll(GetQueueAllRequest request) {
        try {
            List<EpisodeItem> list = queueDao.getAllInQueue(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new DownloadAllUpdated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }
}

package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetDownloadAllRequest;
import fr.github.ethanpod.event.request.GetDownloadTop8Request;
import fr.github.ethanpod.event.updated.DownloadAllUpdated;
import fr.github.ethanpod.event.updated.DownloadTop8Updated;
import fr.github.ethanpod.logic.sql.dao.DownloadDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DownloadRequestHandler {
    private static final Logger logger = LogManager.getLogger(DownloadRequestHandler.class);
    private final DownloadDao downloadDao;

    public DownloadRequestHandler(DownloadDao downloadDao) {
        this.downloadDao = downloadDao;
        // S'enregistre automatiquement sur le bus global
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetDownloadTop8(GetDownloadTop8Request request) {
        try {
            List<EpisodeItem> list = downloadDao.getTop8Download();
            GlobalEventBus.getInstance().post(new DownloadTop8Updated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetDownloadAll(GetDownloadAllRequest request) {
        try {
            List<EpisodeItem> list = downloadDao.getAllDownload(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new DownloadAllUpdated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }
}

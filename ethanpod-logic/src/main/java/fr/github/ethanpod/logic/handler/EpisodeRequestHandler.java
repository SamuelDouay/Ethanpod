package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetEpisodeAllRequest;
import fr.github.ethanpod.event.request.GetEpisodeByPodcastIdRequest;
import fr.github.ethanpod.event.updated.EpisodeAllUpdated;
import fr.github.ethanpod.event.updated.EpisodeByPodcastIdUpdated;
import fr.github.ethanpod.logic.sql.dao.EpisodeDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EpisodeRequestHandler {
    private static final Logger logger = LogManager.getLogger(EpisodeRequestHandler.class);
    private final EpisodeDao episodeDao;

    public EpisodeRequestHandler(EpisodeDao episodeDao) {
        this.episodeDao = episodeDao;
        // S'enregistre automatiquement sur le bus global
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetEpisodeByPodcastId(GetEpisodeByPodcastIdRequest request) {
        try {
            List<EpisodeItem> list = episodeDao.getEpisodeByPodcastId(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new EpisodeByPodcastIdUpdated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetPodcastAll(GetEpisodeAllRequest request) {
        try {
            List<EpisodeItem> list = episodeDao.getEpisodeAll(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new EpisodeAllUpdated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }
}

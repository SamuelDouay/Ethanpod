package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetPodcastByIdRequest;
import fr.github.ethanpod.event.request.GetPodcastReadTop8Request;
import fr.github.ethanpod.event.request.GetSubscriptionsRequest;
import fr.github.ethanpod.event.updated.PodcastByIdUpdated;
import fr.github.ethanpod.event.updated.PodcastReadTop8Updated;
import fr.github.ethanpod.event.updated.SubscriptionAllUpdated;
import fr.github.ethanpod.logic.sql.dao.PodcastDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PodcastRequestHandler {
    private static final Logger logger = LogManager.getLogger(PodcastRequestHandler.class);
    private final PodcastDao podcastDao;

    public PodcastRequestHandler(PodcastDao podcastDao) {
        this.podcastDao = podcastDao;
        // S'enregistre automatiquement sur le bus global
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetPodcastReadTop8(GetPodcastReadTop8Request request) {
        try {
            List<EpisodeItem> list = podcastDao.getTop8PodcastRead();
            GlobalEventBus.getInstance().post(new PodcastReadTop8Updated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetPodacastById(GetPodcastByIdRequest request) {
        try {
            PodcastItem item = podcastDao.getPodcastById(request.getId());
            GlobalEventBus.getInstance().post(new PodcastByIdUpdated(item));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetSubscription(GetSubscriptionsRequest request) {
        try {
            List<NavigationItem> list = podcastDao.getAllSubscription(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new SubscriptionAllUpdated(list));
        } catch (Exception e) {
            logger.error("Erreur ", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }
}

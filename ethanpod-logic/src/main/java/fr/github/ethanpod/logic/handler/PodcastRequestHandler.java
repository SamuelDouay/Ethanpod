package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.event.request.GetPodcastByIdRequest;
import fr.github.ethanpod.event.request.GetPodcastReadTop8Request;
import fr.github.ethanpod.event.request.GetSubscriptionsRequest;
import fr.github.ethanpod.event.updated.PodcastByIdUpdated;
import fr.github.ethanpod.event.updated.PodcastReadTop8Updated;
import fr.github.ethanpod.event.updated.SubscriptionAllUpdated;
import fr.github.ethanpod.logic.sql.dao.PodcastDao;

import java.util.List;

public class PodcastRequestHandler extends BaseRequestHandler {
    private final PodcastDao podcastDao;

    public PodcastRequestHandler(PodcastDao podcastDao) {
        super();
        this.podcastDao = podcastDao;
    }

    @Subscribe
    public void onGetPodcastReadTop8(GetPodcastReadTop8Request request) {
        List<EpisodeItem> list = podcastDao.getTop8PodcastRead();
        postEvent(new PodcastReadTop8Updated(list));
    }

    @Subscribe
    public void onGetPodcastById(GetPodcastByIdRequest request) {
        PodcastItem item = podcastDao.getPodcastById(request.getId());
        postEvent(new PodcastByIdUpdated(item));
    }

    @Subscribe
    public void onGetSubscription(GetSubscriptionsRequest request) {
        List<NavigationItem> list = podcastDao.getAllSubscription(request.getUserDataRequest());
        postEvent(new SubscriptionAllUpdated(list));
    }
}

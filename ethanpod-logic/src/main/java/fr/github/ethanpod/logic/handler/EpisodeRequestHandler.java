package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.request.GetEpisodeAllRequest;
import fr.github.ethanpod.event.request.GetEpisodeByPodcastIdRequest;
import fr.github.ethanpod.event.updated.EpisodeAllUpdated;
import fr.github.ethanpod.event.updated.EpisodeByPodcastIdUpdated;
import fr.github.ethanpod.logic.sql.dao.EpisodeDao;

import java.util.List;

public class EpisodeRequestHandler extends BaseRequestHandler {
    private final EpisodeDao episodeDao;

    public EpisodeRequestHandler(EpisodeDao episodeDao) {
        super();
        this.episodeDao = episodeDao;
    }

    @Subscribe
    public void onGetEpisodeByPodcastId(GetEpisodeByPodcastIdRequest request) {
        List<EpisodeItem> list = episodeDao.getEpisodeByPodcastId(request.getUserDataRequest());
        postEvent(new EpisodeByPodcastIdUpdated(list));
    }

    @Subscribe
    public void onGetPodcastAll(GetEpisodeAllRequest request) {
        List<EpisodeItem> list = episodeDao.getEpisodeAll(request.getUserDataRequest());
        postEvent(new EpisodeAllUpdated(list));
    }
}

package fr.github.ethanpod.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncEpisodeDataService extends AsyncService {

    public AsyncEpisodeDataService() {
        super("EPISODE");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getEpisodeByPodcastId(String serviceId, UserDataRequest userDataRequest) {
        return createRequestFuture(RequestType.GET_EPISODE_BY_PODCAST_ID, serviceId, userDataRequest);

    }
}

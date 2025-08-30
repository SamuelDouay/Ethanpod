package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncPodcastService extends AsyncService {

    public AsyncPodcastService() {
        super("PODCAST");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getTop8PodcastRead(String serviceId) {
        return createRequestFuture(RequestType.GET_PODCAST_READ_TOP8, serviceId);
    }

    public CompletableFuture<RequestResult<PodcastItem>> getPodcastById(String serviceId, Integer id) {
        return createRequestFuture(RequestType.GET_PODCAST_BY_ID, serviceId, id);
    }
}

package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncPodcastService extends AsyncService {

    public AsyncPodcastService() {
        super("PODCAST");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getTop8PodcastRead() {
        return createRequestFuture("GET_TOP8_PODCAST_READ");
    }
}

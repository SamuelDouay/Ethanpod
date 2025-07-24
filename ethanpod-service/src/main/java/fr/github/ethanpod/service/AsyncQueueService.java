package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncQueueService extends AsyncService {
    public AsyncQueueService() {
        super("QUEUE");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getQueueTop8() {
        return createRequestFuture("GET_TOP8_QUEUE");
    }
}

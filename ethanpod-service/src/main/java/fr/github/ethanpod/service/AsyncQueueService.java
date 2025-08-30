package fr.github.ethanpod.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncQueueService extends AsyncService {
    public AsyncQueueService() {
        super("QUEUE");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getQueueTop8(String serviceId) {
        return createRequestFuture(RequestType.GET_QUEUE_TOP8, serviceId);
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getQueueAll(String serviceId, UserDataRequest userDataRequest) {
        return createRequestFuture(RequestType.GET_QUEUE_ALL, serviceId, userDataRequest);
    }
}

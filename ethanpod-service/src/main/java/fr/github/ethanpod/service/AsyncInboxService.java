package fr.github.ethanpod.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncInboxService extends AsyncService {

    public AsyncInboxService() {
        super("INBOX");
    }

    public CompletableFuture<RequestResult<Integer>> getInboxCountAsync(String serviceId) {
        return createRequestFuture(RequestType.GET_INBOX_COUNT, serviceId);
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getTop8InInbox(String serviceId) {
        return createRequestFuture(RequestType.GET_INBOX_TOP8, serviceId);
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getAllInbox(String serviceId, UserDataRequest userDataRequest) {
        return createRequestFuture(RequestType.GET_INBOX_ALL, serviceId, userDataRequest);
    }
}
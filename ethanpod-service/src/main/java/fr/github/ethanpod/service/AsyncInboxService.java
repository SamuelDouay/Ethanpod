package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncInboxService extends AsyncService {

    public AsyncInboxService() {
        super("INBOX");
    }

    public CompletableFuture<RequestResult<Integer>> getInboxCountAsync() {
        return createRequestFuture(RequestType.GET_INBOX_COUNT);
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getTop8InInbox() {
        return createRequestFuture(RequestType.GET_INBOX_TOP8);
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getAllInbox() {
        return createRequestFuture(RequestType.GET_INBOX_ALL);
    }
}
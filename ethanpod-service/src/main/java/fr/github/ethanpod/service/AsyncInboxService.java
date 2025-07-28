package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncInboxService extends AsyncService {

    public AsyncInboxService() {
        super("INBOX");
    }

    public CompletableFuture<RequestResult<Integer>> getInboxCountAsync() {
        return createRequestFuture("GET_COUNT_INBOX");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getTop8InInbox() {
        return createRequestFuture("GET_TOP8_INBOX");
    }

    public CompletableFuture<RequestResult<Boolean>> markAsReadAsync() {
        return createRequestFuture("INBOX_MARK_READ");
    }
}
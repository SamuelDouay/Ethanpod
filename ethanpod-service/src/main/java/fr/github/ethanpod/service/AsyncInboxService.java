package fr.github.ethanpod.service;

import java.util.concurrent.CompletableFuture;

public class AsyncInboxService extends AsyncService {

    public AsyncInboxService() {
        super("INBOX");
    }

    public CompletableFuture<RequestResult<Integer>> getInboxCountAsync() {
        return createRequestFuture("INBOX_COUNT");
    }

    public CompletableFuture<RequestResult<Boolean>> markAsReadAsync() {
        return createRequestFuture("INBOX_MARK_READ");
    }
}
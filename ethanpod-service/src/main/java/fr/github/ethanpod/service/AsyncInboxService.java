package fr.github.ethanpod.service;

import java.util.concurrent.CompletableFuture;

public class AsyncInboxService extends AsyncService {

    public AsyncInboxService() {
        super("INBOX");
    }

    public CompletableFuture<Integer> getInboxCountAsync() {
        return createRequestFuture("INBOX_COUNT_REQUEST");
    }

    public CompletableFuture<Boolean> markAsReadAsync() {
        return createRequestFuture("INBOX_MARK_READ_REQUEST");
    }
}
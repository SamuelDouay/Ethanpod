package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.MessageType;

import java.util.concurrent.CompletableFuture;

public class AsyncInboxService extends AsyncService {

    public AsyncInboxService() {
        super("INBOX");
    }

    public CompletableFuture<Integer> getInboxCountAsync() {
        return createRequestFuture("INBOX_COUNT", MessageType.REQUEST);
    }

    public CompletableFuture<Boolean> markAsReadAsync() {
        return createRequestFuture("INBOX_MARK_READ", MessageType.REQUEST);
    }
}
package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncNavigationService extends AsyncService {

    public AsyncNavigationService() {
        super("NAVIGATION");
    }

    public CompletableFuture<List<NavigationItem>> getListAsync() {
        return createRequestFuture("GET_NAVIGATION_LIST", MessageType.REQUEST);
    }
}
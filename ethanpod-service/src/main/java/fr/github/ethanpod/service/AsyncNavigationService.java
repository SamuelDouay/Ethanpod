package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncNavigationService extends AsyncService {

    public AsyncNavigationService() {
        super("NAVIGATION");
    }

    public CompletableFuture<List<NavigationItem>> getListAsync() {
        return createRequestFuture("GET_NAVIGATION_LIST");
    }
}
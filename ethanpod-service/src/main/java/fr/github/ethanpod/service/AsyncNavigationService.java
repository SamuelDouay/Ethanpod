package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncNavigationService extends AsyncService {

    public AsyncNavigationService() {
        super("NAVIGATION");
    }

    public CompletableFuture<RequestResult<List<NavigationItem>>> getListAsync(String serviceId) {
        return createRequestFuture(RequestType.GET_NAVIGATION_LIST, serviceId);
    }
}
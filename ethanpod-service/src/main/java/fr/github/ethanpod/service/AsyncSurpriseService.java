package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncSurpriseService extends AsyncService {

    public AsyncSurpriseService() {
        super("SURPRISE");
    }

    public CompletableFuture<RequestResult<List<SurpriseItem>>> getListAsync(String serviceId) {
        return createRequestFuture(RequestType.GET_SURPRISE_ALL, serviceId);
    }
}

package fr.github.ethanpod.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.RequestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncHistoryService extends AsyncService {
    public AsyncHistoryService() {
        super("HISTORY");
    }

    public CompletableFuture<RequestResult<List<EpisodeItem>>> getHistoryAll(UserDataRequest userDataRequest) {
        return createRequestFuture(RequestType.GET_HISTORY_ALL, userDataRequest);
    }
}

package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.RequestType;

import java.util.concurrent.CompletableFuture;

public class AsyncDownloadService extends AsyncService {

    public AsyncDownloadService() {
        super("DOWNLOAD");
    }

    public CompletableFuture<RequestResult<Integer>> getDownloadTop8() {
        return createRequestFuture(RequestType.GET_DOWNLOAD_TOP8);
    }
}
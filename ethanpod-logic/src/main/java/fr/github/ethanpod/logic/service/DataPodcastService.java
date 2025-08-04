package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.PodcastDao;

import java.util.concurrent.ExecutorService;

public class DataPodcastService extends DataService {
    private final PodcastDao podcastDao;

    public DataPodcastService(ExecutorService executor) {
        super(executor);
        this.podcastDao = new PodcastDao();
    }

    @Override
    void refreshData() {
        // no
    }

    public void getTop8PodcastRead(String requestId) {
        executeAsync(requestId, ResponseType.PODCAST_READ_TOP8_RESULT,
                podcastDao::getTop8PodcastRead,
                "getting top 8 podcast");
    }
}

package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.ResponseType;
import fr.github.ethanpod.logic.sql.dao.EpisodeDao;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.concurrent.ExecutorService;

public class DataEpisodeService extends DataService {
    private final EpisodeDao episodeDao;

    public DataEpisodeService(ExecutorService executor, DatabaseManager databaseManager) {
        super(executor);
        this.episodeDao = new EpisodeDao(databaseManager);
    }

    @Override
    void refreshData() {
        // no
    }

    public void getEpisodeByPodcastId(String requestId, UserDataRequest userDataRequest) {
        executeAsync(requestId, ResponseType.PODCAST_BY_ID_RESULT,
                () -> episodeDao.getEpisodeByPodcastId(userDataRequest),
                "getting episode by podcast n°" + userDataRequest.data());
    }

    public void getEpisodeAll(String requestId, UserDataRequest userDataRequest) {
        executeAsync(requestId, ResponseType.EPISODE_ALL_RESULT,
                () -> episodeDao.getEpisodeAll(userDataRequest),
                "getting all episode");
    }
}

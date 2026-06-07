package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.query.AllEpisodesQuery;
import fr.github.ethanpod.logic.sql.query.EpisodeByPodcastIdQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class EpisodeDao extends BaseDao {
    public EpisodeDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getEpisodeByPodcastId(UserDataRequest userDataRequest) {
        EpisodeByPodcastIdQuery query = new EpisodeByPodcastIdQuery((Integer) userDataRequest.data(), userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET EPISODE BY PODCAST ID", query.getParameters());
    }

    public List<EpisodeItem> getEpisodeAll(UserDataRequest userDataRequest) {
        AllEpisodesQuery query = new AllEpisodesQuery(userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL EPISODE", query.getParameters());
    }
}

package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class EpisodeDao extends BaseDao {
    public EpisodeDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getEpisodeByPodcastId(UserDataRequest userDataRequest) {
        String sql = EPISODE_BASE_QUERY +
                "WHERE Feeds.id = ? " +
                "ORDER BY FeedItems.pubDate DESC " +
                LIMIT_OFFSET;
        return executeQueryWithParams(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET EPISODE BY PODCAST ID", userDataRequest.data(), userDataRequest.pageSize(), userDataRequest.currentPage());
    }
}

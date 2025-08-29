package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryDao extends BaseDao {
    public HistoryDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getAllInHistory(UserDataRequest userDataRequest) {
        String sql = EPISODE_BASE_QUERY +
                "ORDER BY FeedMedia.playback_completion_date DESC " +
                LIMIT_OFFSET;
        return executeQueryWithParams(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL IN HISTORY", userDataRequest.pageSize(), userDataRequest.currentPage());
    }
}

package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class QueueDao extends BaseDao {
    public QueueDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8InQueue() {
        String sql = EPISODE_BASE_QUERY.replace("LEFT JOIN Queue", "INNER JOIN Queue") +
                "LIMIT 8 ";
        return executeQuery(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET TOP 8 IN QUEUE");
    }

    public List<EpisodeItem> getAllInQueue(UserDataRequest userDataRequest) {
        String sql = EPISODE_BASE_QUERY.replace("LEFT JOIN Queue", "INNER JOIN Queue") +
                LIMIT_OFFSET;
        return executeQueryWithParams(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL IN QUEUE", userDataRequest.pageSize(), userDataRequest.currentPage());
    }
}

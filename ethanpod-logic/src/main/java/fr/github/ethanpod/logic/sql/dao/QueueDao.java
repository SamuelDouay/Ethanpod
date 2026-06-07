package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.query.AllQueueQuery;
import fr.github.ethanpod.logic.sql.query.Top8QueueQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class QueueDao extends BaseDao {
    public QueueDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8InQueue() {
        Top8QueueQuery query = new Top8QueueQuery();
        return executeQuery(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET TOP 8 IN QUEUE");
    }

    public List<EpisodeItem> getAllInQueue(UserDataRequest userDataRequest) {
        AllQueueQuery query = new AllQueueQuery(userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL IN QUEUE");
    }
}

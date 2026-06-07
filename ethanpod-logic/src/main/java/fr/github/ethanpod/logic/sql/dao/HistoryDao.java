package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.query.AllHistoryQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryDao extends BaseDao {
    public HistoryDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getAllInHistory(UserDataRequest userDataRequest) {
        AllHistoryQuery query = new AllHistoryQuery(userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL IN HISTORY");
    }
}

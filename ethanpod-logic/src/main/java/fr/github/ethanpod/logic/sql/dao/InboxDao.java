package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.query.AllInboxQuery;
import fr.github.ethanpod.logic.sql.query.NumberOfInboxQuery;
import fr.github.ethanpod.logic.sql.query.Top8InboxQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class InboxDao extends BaseDao {

    public InboxDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public int getNumberOfInbox() {
        NumberOfInboxQuery query = new NumberOfInboxQuery();
        return executeQuery(query, rs -> rs.next() ? rs.getInt("unread_count") : 0, 0, "GET NUMBER IN INBOX");
    }

    public List<EpisodeItem> getTop8InInbox() {
        Top8InboxQuery query = new Top8InboxQuery();
        return executeQuery(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET TOP 8 IN INBOX");
    }

    public List<EpisodeItem> getAllInInbox(UserDataRequest userDataRequest) {
        AllInboxQuery query = new AllInboxQuery(userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL IN INBOX");
    }
}

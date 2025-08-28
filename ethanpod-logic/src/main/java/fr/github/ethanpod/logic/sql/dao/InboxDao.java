package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class InboxDao extends BaseDao {

    public InboxDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public int getNumberOfInbox() {
        String sql = "SELECT COUNT(*) as unread_count " +
                "FROM FeedItems AS items " +
                "WHERE items.read = -1 ";
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("unread_count") : 0, 0, "GET NUMBER IN INBOX");
    }

    public List<EpisodeItem> getTop8InInbox() {
        String sql = EPISODE_BASE_QUERY +
                "WHERE FeedItems.read = -1 " +
                "ORDER BY FeedItems.pubDate DESC " +
                "LIMIT 8 ";
        return executeQuery(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET TOP 8 IN INBOX");
    }

    public List<EpisodeItem> getAllInInbox(UserDataRequest userDataRequest) {
        String sql = EPISODE_BASE_QUERY +
                "WHERE FeedItems.read = -1 " +
                "ORDER BY FeedItems.pubDate DESC " +
                LIMIT_OFFSET;
        return executeQueryWithParams(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL IN INBOX", userDataRequest.pageSize(), userDataRequest.currentPage());
    }
}

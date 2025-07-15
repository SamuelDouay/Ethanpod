package fr.github.ethanpod.logic.sql.dao;

public class InboxDao extends BaseDao {
    private static final String UNREAD_CONDITION = "CASE WHEN items.read = -1 THEN 1 END";

    public InboxDao() {
        // no parameter
    }

    public int getNumberOfInbox() {
        String sql = "SELECT COUNT(" + UNREAD_CONDITION + ") as unread_count FROM FeedItems AS items";
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("unread_count") : 0, 0);
    }
}

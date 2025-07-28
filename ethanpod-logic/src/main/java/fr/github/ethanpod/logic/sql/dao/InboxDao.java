package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class InboxDao extends BaseDao {
    private static final String UNREAD_CONDITION = "CASE WHEN items.read = -1 THEN 1 END";
    private static final String NEWS_ITEMS_JOIN = "FROM FeedItems feed INNER JOIN FeedMedia fm ON fm.feeditem = feed.id ";
    private static final String LIMIT_8 = "LIMIT 8";

    public InboxDao() {
        // no parameter
    }

    public int getNumberOfInbox() {
        String sql = "SELECT COUNT(" + UNREAD_CONDITION + ") as unread_count FROM FeedItems AS items";
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("unread_count") : 0, 0);
    }

    public List<EpisodeItem> getTop8InInbox() {
        String sql = "SELECT feed.title as title, feed.pubDate as date, feed.image_url as image_url, fm.filesize as size, fm.duration as duration " +
                NEWS_ITEMS_JOIN + " " +
                "WHERE feed.read = -1 " +
                "ORDER BY feed.pubDate DESC " +
                LIMIT_8;

        return executeQuery(sql, rs -> {
            List<EpisodeItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new EpisodeItem(rs.getString("image_url"),
                        false,
                        rs.getString("title"),
                        Converter.timestampToDate(rs.getLong("date")),
                        Converter.timestampToDate(rs.getLong("date")),
                        Converter.getSize(rs.getLong("size")),
                        false));
            }
            return result;
        }, new ArrayList<>());
    }
}

package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class EpisodeDao extends BaseDao {
    private static final String QUEUE_ITEMS_JOIN = "FROM Feeds AS feed INNER JOIN Queue queue ON queue.feeditem = feed.id ";
    private static final String NEWS_ITEMS_JOIN = "FROM FeedItems feed INNER JOIN FeedMedia fm ON fm.feeditem = feed.id ";
    private static final String LIMIT_8 = "LIMIT 8";

    public EpisodeDao(DatabaseManager databaseManager) {
        super(databaseManager);
        // no parameter
    }

    public List<EpisodeItem> getTop8InQueue() {
        String sql = "SELECT feed.title as title, feed.pubDate as date, feed.image_url as image_url " +
                QUEUE_ITEMS_JOIN +
                LIMIT_8;

        return executeQuery(sql, rs -> {
            List<EpisodeItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new EpisodeItem(
                        rs.getString("image_url"),
                        false,
                        rs.getString("title"),
                        null,
                        rs.getString("date"),
                        null,
                        false
                ));
            }
            return result;
        }, new ArrayList<>());
    }

    public List<EpisodeItem> getNewsTop8() {
        String sql = "SELECT feed.title as title, feed.pubDate as date, feed.image_url as image_url, fm.filesize as size " +
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
                        null,
                        Converter.timestampToDate(rs.getLong("date")),
                        Converter.getSize(rs.getLong("size")),
                        false));
            }
            return result;
        }, new ArrayList<>());
    }
}

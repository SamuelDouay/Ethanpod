package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;

import java.util.ArrayList;
import java.util.List;

public class PodcastDao extends BaseDao {
    private static final String FEED_ITEMS_JOIN = "INNER JOIN FeedItems ON FeedItems.feed = Feeds.id ";
    private static final String LIMIT_8 = "LIMIT 8";

    public PodcastDao() {
        // no param
    }

    public List<EpisodeItem> getTop8PodcastRead() {
        String sql = "SELECT Feeds.title, Feeds.image_url , count(*) as item_read FROM Feeds " +
                FEED_ITEMS_JOIN +
                "WHERE FeedItems.read = 1 " +
                "GROUP BY Feeds.title " +
                "ORDER BY item_read DESC " +
                LIMIT_8;

        return executeQuery(sql, rs -> {
            List<EpisodeItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new EpisodeItem(
                        rs.getString("image_url"),
                        false,
                        rs.getString("title"),
                        null,
                        null,
                        null,
                        false
                ));
            }
            return result;
        }, new ArrayList<>());
    }
}

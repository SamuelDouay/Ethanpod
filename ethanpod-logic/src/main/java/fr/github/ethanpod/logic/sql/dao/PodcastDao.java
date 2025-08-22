package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class PodcastDao extends BaseDao {
    private static final String FEED_ITEMS_JOIN = "INNER JOIN FeedItems ON FeedItems.feed = Feeds.id ";
    private static final String LIMIT_8 = "LIMIT 8";

    public PodcastDao(DatabaseManager databaseManager) {
        super(databaseManager);
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
                                rs.getString("title"),
                                null,
                                false,
                                null,
                                rs.getString("image_url"),
                                null,
                                null,
                                false,
                                false,
                                false
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET TOP 8 IN PODCAST READ");
    }

    public PodcastItem getPodcastById(Integer id) {
        String sql = "SELECT feed.title, feed.author, feed.description, feed.image_url " +
                "FROM Feeds feed " +
                "WHERE feed.id = ?";

        return executeQueryWithParams(sql, rs -> {
                    if (rs.next()) {
                        return new PodcastItem(
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getString("author"),
                                rs.getString("image_url")
                        );
                    }
                    return null;
                },
                new PodcastItem(null, null, null, null),
                "GET PODCAST NUMBER " + id,
                id);
    }
}

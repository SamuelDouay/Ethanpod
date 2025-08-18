package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class QueueDao extends BaseDao {
    private static final String QUEUE_ITEMS_JOIN = "FROM FeedItems AS items INNER JOIN Queue queue ON queue.feeditem = items.id ";
    private static final String LIMIT_8 = "LIMIT 8";

    public QueueDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8InQueue() {
        String sql = "SELECT items.title as title, items.pubDate as date, items.image_url as items_image, feeds.image_url as feed_image " +
                QUEUE_ITEMS_JOIN +
                "INNER JOIN Feeds feeds ON feeds.id  = items.feed " +
                LIMIT_8;

        return executeQuery(sql, rs -> {
                    List<EpisodeItem> result = new ArrayList<>();
                    while (rs.next()) {
                        String imageUrl = rs.getString("items_image") != null ? rs.getString("items_image") : rs.getString("feed_image");
                        result.add(new EpisodeItem(
                                imageUrl,
                                false,
                                rs.getString("title"),
                                null,
                                Converter.timestampToDate(rs.getLong("date")),
                                null,
                                false
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET TOP 8 IN QUEUE");
    }
}

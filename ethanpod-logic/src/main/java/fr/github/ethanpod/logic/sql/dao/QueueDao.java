package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class QueueDao extends BaseDao {
    private static final String QUEUE_ITEMS_JOIN = "FROM FeedItems AS items INNER JOIN Queue queue ON queue.feeditem = items.id ";
    private static final String LIMIT_8 = "LIMIT 8";

    public QueueDao() {
        // no parameter
    }

    public List<EpisodeItem> getTop8InQueue() {
        String sql = "SELECT items.title as title, items.pubDate as date, items.image_url as image_url " +
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
                        Converter.timestampToDate(rs.getLong("date")),
                        null,
                        false
                ));
            }
            return result;
        }, new ArrayList<>());
    }
}

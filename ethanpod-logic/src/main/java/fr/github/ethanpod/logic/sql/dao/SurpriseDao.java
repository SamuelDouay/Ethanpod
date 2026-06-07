package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.logic.sql.query.SurpriseListQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class SurpriseDao extends BaseDao {
    public SurpriseDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<SurpriseItem> getList() {
        String sql = "SELECT FeedItems.title, Feeds.title, FeedItems.image_url as item_image, Feeds.image_url as feed_image " +
                "FROM FeedItems " +
                "INNER JOIN FeedMedia ON FeedMedia.feeditem = FeedItems.id " +
                "INNER JOIN Feeds ON Feeds.id = FeedItems.feed " +
                "WHERE FeedItems.read != 1 " +
                "ORDER BY RANDOM() " +
                "LIMIT 9";

        SurpriseListQuery query = new SurpriseListQuery();
        return executeQuery(query, rs -> {
                    List<SurpriseItem> result = new ArrayList<>();
                    while (rs.next()) {
                        String imageUrl = getImageUrl(rs.getString(3), rs.getString(4));
                        result.add(new SurpriseItem(
                                rs.getString(1),
                                rs.getString(2),
                                imageUrl
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET LIT OF SURPRISE");
    }
}

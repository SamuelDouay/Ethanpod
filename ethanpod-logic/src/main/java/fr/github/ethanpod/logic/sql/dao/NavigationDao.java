package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class NavigationDao extends BaseDao {
    private static final String FEED_ITEMS_JOIN = "FROM Feeds AS feed INNER JOIN FeedItems AS items ON items.feed = feed.id";

    public NavigationDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<NavigationItem> getList() {
        String sql = "SELECT feed.title as title, feed.image_url as image_url, " +
                "COUNT( * ) as unread_count " +
                FEED_ITEMS_JOIN + " " +
                "WHERE items.read = -1 " +
                "GROUP BY feed.id " +
                "ORDER BY unread_count DESC, feed.title ASC";

        return executeQuery(sql, rs -> {
                    List<NavigationItem> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(new NavigationItem(
                                rs.getString("image_url"),
                                rs.getString("title"),
                                rs.getInt("unread_count"),
                                false
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET LIT OF PODCASTS");
    }
}

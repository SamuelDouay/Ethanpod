package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class NavigationDao extends BaseDao {
    public NavigationDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<NavigationItem> getList() {
        String sql = "SELECT f.title, f.image_url, " +
                "(SELECT COUNT(*) FROM FeedItems fi WHERE fi.feed = f.id AND fi.read = -1) as unread_count " +
                " FROM Feeds f ORDER BY unread_count DESC, f.title ASC";

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

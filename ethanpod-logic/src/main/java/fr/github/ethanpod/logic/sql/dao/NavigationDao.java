package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.logic.sql.query.AllNavigationItemsQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class NavigationDao extends BaseDao {
    public NavigationDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<NavigationItem> getList() {
        AllNavigationItemsQuery query = new AllNavigationItemsQuery();
        return executeQuery(query, rs -> {
                    List<NavigationItem> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(new NavigationItem(
                                rs.getString("image_url"),
                                rs.getString("title"),
                                rs.getInt("unread_count"),
                                false,
                                rs.getInt("id")
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET LIT OF PODCASTS");
    }
}

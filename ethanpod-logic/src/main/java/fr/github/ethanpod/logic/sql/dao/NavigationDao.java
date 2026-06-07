package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.logic.sql.mapper.ResultMappers;
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
        return executeQuery(query, ResultMappers.navigationItemListMapper(), new ArrayList<>(),
                "GET LIT OF PODCASTS");
    }
}

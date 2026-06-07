package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.logic.sql.mapper.ResultMappers;
import fr.github.ethanpod.logic.sql.query.SurpriseListQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class SurpriseDao extends BaseDao {
    public SurpriseDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<SurpriseItem> getList() {
        SurpriseListQuery query = new SurpriseListQuery();
        return executeQuery(query, ResultMappers.surpriseItemListMapper(), new ArrayList<>(), "GET LIT OF SURPRISE");
    }
}

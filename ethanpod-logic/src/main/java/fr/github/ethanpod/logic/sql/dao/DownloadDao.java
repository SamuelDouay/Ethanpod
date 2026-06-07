package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.query.AllDownloadQuery;
import fr.github.ethanpod.logic.sql.query.Top8DownloadQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class DownloadDao extends BaseDao {
    public DownloadDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8Download() {
        Top8DownloadQuery query = new Top8DownloadQuery();
        return executeQuery(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET TOP 8 DOWNLOAD");
    }

    public List<EpisodeItem> getAllDownload(UserDataRequest userDataRequest) {
        AllDownloadQuery query = new AllDownloadQuery(userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL DOWNLOAD");
    }
}

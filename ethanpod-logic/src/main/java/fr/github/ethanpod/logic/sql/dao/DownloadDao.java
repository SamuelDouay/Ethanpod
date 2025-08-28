package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class DownloadDao extends BaseDao {
    public DownloadDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8Download() {
        String sql = EPISODE_BASE_QUERY +
                "WHERE FeedMedia.downloaded != 0 " +
                "ORDER BY FeedMedia.downloaded DESC " +
                "LIMIT 8 ";
        return executeQuery(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET TOP 8 DOWNLOAD");
    }

    public List<EpisodeItem> getAllDownload(UserDataRequest userDataRequest) {
        String sql = EPISODE_BASE_QUERY +
                "WHERE FeedMedia.downloaded != 0 " +
                "ORDER BY FeedMedia.downloaded DESC " +
                LIMIT_OFFSET;
        return executeQueryWithParams(sql, EPISODE_LIST_MAPPER, new ArrayList<>(), "GET ALL DOWNLOAD", userDataRequest.pageSize(), userDataRequest.currentPage());
    }
}

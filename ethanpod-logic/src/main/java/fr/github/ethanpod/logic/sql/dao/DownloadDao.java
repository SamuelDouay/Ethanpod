package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class DownloadDao extends BaseDao {
    public DownloadDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8Download() {
        String sql = "SELECT feeds.title as title, feeds.pubDate as date, feeds.image_url as image_url, feed.image_url as feed_image, feedMedia.filesize as size, feedMedia.duration as duration " +
                "FROM FeedMedia feedMedia " +
                "INNER JOIN FeedItems feeds ON Feeds.id = feedMedia.feeditem " +
                "INNER JOIN Feeds feed ON feed.id = feeds.feed " +
                "WHERE FeedMedia.file_url NOT NULL " +
                "ORDER BY FeedMedia.downloaded DESC " +
                "LIMIT 8";
        return executeQuery(sql, rs -> {
                    List<EpisodeItem> result = new ArrayList<>();
                    while (rs.next()) {
                        String imageUrl = rs.getString("image_url") != null ? rs.getString("image_url") : rs.getString("feed_image");
                        result.add(new EpisodeItem(imageUrl,
                                false,
                                rs.getString("title"),
                                Converter.convertToHHMMSS(rs.getLong("duration")),
                                Converter.timestampToDate(rs.getLong("date")),
                                Converter.getSize(rs.getLong("size")),
                                false));
                    }
                    return result;
                },
                new ArrayList<>(),
                "GET TOP 8 DOWNLOAD");
    }
}

package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class EpisodeDao extends BaseDao {
    public EpisodeDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getEpisodeByPodcastId(Integer id) {
        String sql = "SELECT FeedItems.title as title, FeedItems.pubDate as date, FeedItems.image_url as image_url, Feeds.image_url as feed_image, FeedMedia.filesize as size, FeedMedia.duration as duration " +
                "FROM FeedItems " +
                "INNER JOIN FeedMedia ON FeedMedia.feeditem = FeedItems.id " +
                "INNER JOIN Feeds ON Feeds.id = FeedItems.feed " +
                "WHERE Feeds.id = ? " +
                "ORDER BY FeedItems.pubDate DESC ";
        return executeQueryWithParams(sql, rs -> {
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
                "GET EPISODE BY PODCAST ID", id);
    }
}

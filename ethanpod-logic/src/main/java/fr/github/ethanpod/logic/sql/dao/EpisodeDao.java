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
        String sql = "SELECT FeedItems.title, FeedItems.pubDate, FeedItems.read, FeedItems.description, FeedItems.image_url as item_image, FeedMedia.duration, FeedMedia.filesize, Feeds.image_url as feed_image, Queue.id as queue, Favorites.id as favorie " +
                "FROM FeedItems " +
                "INNER JOIN FeedMedia ON FeedMedia.feeditem = FeedItems.id " +
                "INNER JOIN Feeds ON Feeds.id = FeedItems.feed " +
                "LEFT JOIN Queue on Queue.feeditem = FeedItems.id " +
                "LEFT JOIN Favorites ON Favorites.feeditem = FeedItems.id " +
                "WHERE Feeds.id = ? " +
                "ORDER BY FeedItems.pubDate DESC ";
        return executeQueryWithParams(sql, rs -> {
                    List<EpisodeItem> result = new ArrayList<>();
                    while (rs.next()) {
                        String imageUrl = rs.getString(5) != null ? rs.getString(5) : rs.getString(8);
                        result.add(new EpisodeItem(
                                rs.getString(1),
                                Converter.timestampToDate(rs.getLong(2)),
                                rs.getInt(3) == 1,
                                rs.getString(4),
                                imageUrl,
                                Converter.convertToHHMMSS(rs.getLong(6)),
                                Converter.getSize(rs.getLong(7)),
                                rs.getString(9) != null,
                                rs.getInt(3) == -1,
                                rs.getString(10) != null
                        ));
                    }
                    return result;
                },
                new ArrayList<>(),
                "GET EPISODE BY PODCAST ID", id);
    }
}

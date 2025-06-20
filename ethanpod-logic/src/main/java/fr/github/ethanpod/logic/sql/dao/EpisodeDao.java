package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.setting.Connect;
import fr.github.ethanpod.util.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EpisodeDao {
    private static final Logger logger = LogManager.getLogger(EpisodeDao.class);

    public EpisodeDao() {
        // no parameter
    }

    public List<EpisodeItem> getTop8InQueue() {
        String sql = "SELECT feed.title as title, feed.pubDate as date, feed.image_url as image_url " +
                "FROM FeedItems feed " +
                "INNER JOIN Queue queue ON queue.feeditem = feed.id " +
                "LIMIT 8";

        List<EpisodeItem> res = new ArrayList<>();

        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {
            // Parcours direct sans essayer de se déplacer dans le ResultSet
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String date = resultSet.getString("date");
                String imgUrl = resultSet.getString("image_url");
                res.add(new EpisodeItem(imgUrl, false, title, null, date, null, false));
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de l'exécution de la requête getTop8InQueue: {}", e.getMessage(), e);
        }
        return res;
    }

    public List<EpisodeItem> getNewsTop8() {
        String sql = "SELECT feed.title as title, feed.pubDate as date, feed.image_url as image_url, fm.filesize as size " +
                "FROM FeedItems feed " +
                "INNER JOIN FeedMedia fm ON fm.feeditem = feed.id " +
                "WHERE feed.read = -1 " +
                "ORDER BY feed.pubDate DESC " +
                "LIMIT 8";

        List<EpisodeItem> res = new ArrayList<>();

        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {
            // Parcours direct sans essayer de se déplacer dans le ResultSet
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String date = Converter.timestampToDate(resultSet.getLong("date"));
                String imgUrl = resultSet.getString("image_url");
                String size = Converter.getSize(resultSet.getLong("size"));
                res.add(new EpisodeItem(imgUrl, false, title, null, date, size, false));
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de l'exécution de la requête getTop8InQueue: {}", e.getMessage(), e);
        }
        return res;
    }
}

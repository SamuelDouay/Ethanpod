package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.logic.sql.setting.Connect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NavigationDao {
    private static final Logger logger = LogManager.getLogger(NavigationDao.class);

    public NavigationDao() {
        // no parameter
    }

    public List<NavigationItem> getList() {
        String sql = "SELECT feed.title as title, " +
                "feed.image_url as image_url, " +
                "COUNT(CASE WHEN items.read = -1 THEN 1 END) as unread_count " +
                "FROM Feeds AS feed " +
                "INNER JOIN FeedItems AS items ON items.feed = feed.id " +
                "GROUP BY feed.id, feed.title, feed.image_url " +
                "ORDER BY unread_count DESC, feed.title ASC";


        List<NavigationItem> res = new ArrayList<>();

        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {
            // Parcours direct sans essayer de se déplacer dans le ResultSet
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String imgUrl = resultSet.getString("image_url");
                int unreadCount = resultSet.getInt("unread_count");
                res.add(new NavigationItem(imgUrl, title, unreadCount, false));
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de l'exécution de la requête getList: {}", e.getMessage(), e);
        }
        return res;
    }

    public int getNumberOfInbox() {
        String sql = "SELECT COUNT(CASE WHEN items.read = -1 THEN 1 END) as unread_count " +
                "FROM  FeedItems AS items";

        int unreadCount = 0;

        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {
            // Parcours direct sans essayer de se déplacer dans le ResultSet
            while (resultSet.next()) {
                unreadCount = resultSet.getInt("unread_count");
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de l'exécution de la requête getList: {}", e.getMessage(), e);
        }
        return unreadCount;
    }
}

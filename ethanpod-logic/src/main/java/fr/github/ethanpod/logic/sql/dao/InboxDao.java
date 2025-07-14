package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.logic.sql.setting.Connect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InboxDao {
    private static final Logger logger = LogManager.getLogger(InboxDao.class);

    public InboxDao() {
        // no parameter
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

package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.exception.technical.DatabaseException;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao {
    protected static final Logger logger = LogManager.getLogger(BaseDao.class);
    // Requête SQL commune pour récupérer les épisodes
    protected static final String EPISODE_BASE_QUERY =
            "SELECT FeedItems.title, FeedItems.pubDate, FeedItems.read, FeedItems.description, " +
                    "FeedItems.image_url as item_image, FeedMedia.duration, FeedMedia.filesize, " +
                    "Feeds.image_url as feed_image, Queue.id as queue, Favorites.id as favorie " +
                    "FROM FeedItems " +
                    "INNER JOIN FeedMedia ON FeedMedia.feeditem = FeedItems.id " +
                    "INNER JOIN Feeds ON Feeds.id = FeedItems.feed " +
                    "LEFT JOIN Queue on Queue.feeditem = FeedItems.id " +
                    "LEFT JOIN Favorites ON Favorites.feeditem = FeedItems.id ";
    // Mapper commun pour créer les EpisodeItem
    protected static final ResultSetMapper<List<EpisodeItem>> EPISODE_LIST_MAPPER = rs -> {
        List<EpisodeItem> result = new ArrayList<>();
        while (rs.next()) {
            String imageUrl = getImageUrl(rs.getString(5), rs.getString(8));
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
    };
    private final DatabaseManager databaseManager;

    protected BaseDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    protected static String getImageUrl(String itemImage, String feedImage) {
        String imageUrl;
        if (itemImage != null && !itemImage.trim().isEmpty()) {
            // Si item_image existe, construire l'URL complète
            if (itemImage.startsWith("http")) {
                imageUrl = itemImage; // Déjà une URL complète
            } else {
                // Extraire le domaine de base depuis feed_image
                String baseUrl = getUrl(feedImage, itemImage);
                imageUrl = baseUrl + itemImage;
            }
        } else {
            // Sinon utiliser feed_image (qui est déjà une URL complète)
            imageUrl = feedImage;
        }
        return imageUrl;
    }

    private static String getUrl(String feedImage, String itemImage) {
        if (feedImage != null) {
            try {
                URL url = new URI(feedImage).toURL();
                return url.getProtocol() + "://" + url.getHost();
            } catch (MalformedURLException | URISyntaxException _) {
                return itemImage;
            }
        } else {
            return itemImage;
        }
    }

    private void logMetrics(String sql, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        if (executionTime > 100) {
            logger.warn("Slow SQL Query detected: {} executed in {}ms", sql, executionTime);
        } else {
            logger.debug("SQL Query executed in {}ms: {}", executionTime, sql);
        }
    }

    protected <T> T executeQuery(String sql, ResultSetMapper<T> mapper, T defaultValue, String context) {
        long startTime = System.currentTimeMillis();
        try {
            return databaseManager.executeWithConnection(conn -> {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    T result = mapper.map(rs);
                    logMetrics(sql, startTime);
                    return result;

                } catch (SQLException e) {
                    logger.error("Erreur SQL [{}]: {}", sql, e.getMessage(), e);
                    return defaultValue;
                }
            }, context);

        } catch (DatabaseException e) {
            logger.error("Erreur base de données lors de [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected <T> T executeQueryWithParams(String sql, ResultSetMapper<T> mapper, T defaultValue, String context, Object... params) {
        long startTime = System.currentTimeMillis();
        try {
            return databaseManager.executeWithConnection(conn -> {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    // Paramètres
                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }

                    try (ResultSet rs = stmt.executeQuery()) {
                        T result = mapper.map(rs);
                        logMetrics(sql, startTime);
                        return result;
                    }
                }
            }, context);

        } catch (DatabaseException e) {
            logger.error("Erreur lors de la requête [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected int executeUpdate(String sql, String context, Object... params) {
        long startTime = System.currentTimeMillis();
        try {
            return databaseManager.executeWithConnection(conn -> {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    // Paramètres
                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }

                    int rowsAffected = stmt.executeUpdate();
                    logMetrics(sql, startTime);
                    return rowsAffected;
                }
            }, context);

        } catch (DatabaseException e) {
            logger.error("Erreur lors de la requête [{}]: {}", sql, e.getMessage(), e);
            return 0;
        }
    }

    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}

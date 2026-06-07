package fr.github.ethanpod.logic.sql.mapper;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.logic.sql.dao.BaseDao;
import fr.github.ethanpod.util.Converter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class ResultMappers {

    private ResultMappers() {
        // Utility class
    }

    private static String getImageUrl(String itemImage, String feedImage) {
        String imageUrl;
        if (itemImage != null && !itemImage.trim().isEmpty()) {
            if (itemImage.startsWith("http")) {
                imageUrl = itemImage;
            } else {
                String baseUrl = getUrl(feedImage, itemImage);
                imageUrl = baseUrl + itemImage;
            }
        } else {
            imageUrl = feedImage;
        }
        return imageUrl;
    }

    public static EpisodeItem mapToEpisodeItem(ResultSet rs) throws SQLException {
        return new EpisodeItem(
                rs.getString("episodeTitle"),
                Converter.timestampToDate(rs.getLong("pubDate")),
                rs.getInt("readStatus") == 1,
                rs.getString("episodeDescription"),
                getImageUrl(rs.getString("itemImage"), rs.getString("feedImage")),
                Converter.convertToHHMMSS(rs.getLong("duration")),
                Converter.getSize(rs.getLong("filesize")),
                rs.getString("queueId") != null,
                rs.getInt("readStatus") == -1,
                rs.getString("favoriteId") != null
        );
    }

    public static BaseDao.ResultSetMapper<List<EpisodeItem>> episodeListMapper() {
        return rs -> {
            List<EpisodeItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapToEpisodeItem(rs));
            }
            return result;
        };
    }

    public static NavigationItem mapToNavigationItem(ResultSet rs) throws SQLException {
        return new NavigationItem(
                rs.getString("imageUrl"),
                rs.getString("podcastTitle"),
                rs.getInt("unreadCount"),
                false,
                rs.getInt("podcastId")
        );
    }

    public static BaseDao.ResultSetMapper<List<NavigationItem>> navigationItemListMapper() {
        return rs -> {
            List<NavigationItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapToNavigationItem(rs));
            }
            return result;
        };
    }

    public static PodcastItem mapToPodcastItem(ResultSet rs) throws SQLException {
        return new PodcastItem(
                rs.getString("podcastTitle"),
                rs.getString("description"),
                rs.getString("author"),
                rs.getString("imageUrl")
        );
    }

    public static BaseDao.ResultSetMapper<PodcastItem> podcastItemMapper() {
        return rs -> {
            if (rs.next()) {
                return mapToPodcastItem(rs);
            }
            return null;
        };

    }

    public static BaseDao.ResultSetMapper<Integer> countMapper() {
        return rs -> rs.next() ? rs.getInt("unreadCount") : 0;
    }

    public static SurpriseItem mapToSurpriseItem(ResultSet rs) throws SQLException {
        return new SurpriseItem(
                rs.getString("episodeTitle"),
                rs.getString("podcastTitle"),
                getImageUrl(rs.getString("itemImage"), rs.getString("feedImage"))
        );
    }

    public static BaseDao.ResultSetMapper<List<SurpriseItem>> surpriseItemListMapper() {
        return rs -> {
            List<SurpriseItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapToSurpriseItem(rs));
            }
            return result;
        };
    }

    public static EpisodeItem mapToMinimalEpisodeItem(ResultSet rs) throws SQLException {
        return new EpisodeItem(
                rs.getString("podcastTitle"),
                null,  // No publication date
                false, // Not read
                null,  // No description
                rs.getString("imageUrl"),
                null,  // No duration
                null,  // No filesize
                false, // Not in queue
                false, // Not unread
                false  // Not favorite
        );
    }

    public static BaseDao.ResultSetMapper<List<EpisodeItem>> minimalEpisodeListMapper() {
        return rs -> {
            List<EpisodeItem> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapToMinimalEpisodeItem(rs));
            }
            return result;
        };
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
}

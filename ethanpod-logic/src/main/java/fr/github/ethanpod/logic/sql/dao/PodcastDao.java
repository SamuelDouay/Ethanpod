package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.logic.sql.query.AllSubscriptionsQuery;
import fr.github.ethanpod.logic.sql.query.PodcastByIdQuery;
import fr.github.ethanpod.logic.sql.query.Top8PodcastReadQuery;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class PodcastDao extends BaseDao {

    public PodcastDao(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public List<EpisodeItem> getTop8PodcastRead() {
        Top8PodcastReadQuery query = new Top8PodcastReadQuery();
        return executeQuery(query, rs -> {
                    List<EpisodeItem> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(new EpisodeItem(
                                rs.getString("title"),
                                null,
                                false,
                                null,
                                rs.getString("image_url"),
                                null,
                                null,
                                false,
                                false,
                                false
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET TOP 8 IN PODCAST READ");
    }

    public PodcastItem getPodcastById(Integer id) {
        PodcastByIdQuery query = new PodcastByIdQuery(id);

        return executeQueryWithParams(query, rs -> {
                    if (rs.next()) {
                        return new PodcastItem(
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getString("author"),
                                rs.getString("image_url")
                        );
                    }
                    return null;
                },
                new PodcastItem(null, null, null, null),
                "GET PODCAST NUMBER " + id);
    }

    public List<NavigationItem> getAllSubscription(UserDataRequest userDataRequest) {

        AllSubscriptionsQuery query = new AllSubscriptionsQuery(userDataRequest.pageSize(), userDataRequest.currentPage());

        return executeQueryWithParams(query, rs -> {
                    List<NavigationItem> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(new NavigationItem(
                                rs.getString("image_url"),
                                rs.getString("title"),
                                rs.getInt("unread_count"),
                                false,
                                rs.getInt("id")
                        ));
                    }
                    return result;
                }, new ArrayList<>(),
                "GET LIT OF SUBSCRIPTION");
    }

}

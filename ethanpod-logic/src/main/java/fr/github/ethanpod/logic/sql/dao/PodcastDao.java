package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.item.PodcastItem;
import fr.github.ethanpod.logic.sql.mapper.ResultMappers;
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
        return executeQuery(query, ResultMappers.minimalEpisodeListMapper(), new ArrayList<>(), "GET TOP 8 IN PODCAST READ");
    }

    public PodcastItem getPodcastById(Integer id) {
        PodcastByIdQuery query = new PodcastByIdQuery(id);

        return executeQueryWithParams(query, ResultMappers.podcastItemMapper(),
                new PodcastItem(null, null, null, null),
                "GET PODCAST NUMBER " + id);
    }

    public List<NavigationItem> getAllSubscription(UserDataRequest userDataRequest) {
        AllSubscriptionsQuery query = new AllSubscriptionsQuery(userDataRequest.pageSize(), userDataRequest.currentPage());
        return executeQueryWithParams(query, ResultMappers.navigationItemListMapper(), new ArrayList<>(), "GET LIT OF SUBSCRIPTION");
    }

}

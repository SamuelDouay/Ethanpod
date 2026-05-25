package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetNavigationRequest;
import fr.github.ethanpod.event.updated.NavigationAllUpdated;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;

import java.util.List;

public class NavigationRequestHandler extends BaseRequestHandler {
    private final NavigationDao navigationDao; // ou tout autre DAO adapté

    public NavigationRequestHandler(NavigationDao navigationDao) {
        super();
        this.navigationDao = navigationDao;
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetNavigationList(GetNavigationRequest request) {
        List<NavigationItem> items = navigationDao.getList();
        postEvent(new NavigationAllUpdated(items));
    }
}
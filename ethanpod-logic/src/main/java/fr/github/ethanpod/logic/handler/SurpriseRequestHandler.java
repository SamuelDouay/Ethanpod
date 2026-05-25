package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.SurpriseItem;
import fr.github.ethanpod.event.request.GetSurpriseRequest;
import fr.github.ethanpod.event.updated.SurpriseAllUpdated;
import fr.github.ethanpod.logic.sql.dao.SurpriseDao;

import java.util.List;

public class SurpriseRequestHandler extends BaseRequestHandler {
    private final SurpriseDao surpriseDao; // ou tout autre DAO adapté

    public SurpriseRequestHandler(SurpriseDao surpriseDao) {
        super();
        this.surpriseDao = surpriseDao;
    }

    @Subscribe
    public void onGetNavigationList(GetSurpriseRequest request) {
        List<SurpriseItem> items = surpriseDao.getList();
        postEvent(new SurpriseAllUpdated(items));
    }
}
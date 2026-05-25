package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.request.GetHistoryAllRequest;
import fr.github.ethanpod.event.updated.HistoryAllUpdated;
import fr.github.ethanpod.logic.sql.dao.HistoryDao;

import java.util.List;

public class HistoryRequestHandler extends BaseRequestHandler {
    private final HistoryDao historyDao;

    public HistoryRequestHandler(HistoryDao historyDao) {
        super();
        this.historyDao = historyDao;
    }

    @Subscribe
    public void onGetHistoryAll(GetHistoryAllRequest request) {
        List<EpisodeItem> list = historyDao.getAllInHistory(request.getUserDataRequest());
        postEvent(new HistoryAllUpdated(list));
    }
}

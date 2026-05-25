package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.request.GetQueueAllRequest;
import fr.github.ethanpod.event.request.GetQueueTop8Request;
import fr.github.ethanpod.event.updated.QueueAllUpdated;
import fr.github.ethanpod.event.updated.QueueTop8Updated;
import fr.github.ethanpod.logic.sql.dao.QueueDao;

import java.util.List;

public class QueueRequestHandler extends BaseRequestHandler {
    private final QueueDao queueDao;

    public QueueRequestHandler(QueueDao queueDao) {
        super();
        this.queueDao = queueDao;
    }

    @Subscribe
    public void onGetQueueTop8(GetQueueTop8Request request) {
        List<EpisodeItem> list = queueDao.getTop8InQueue();
        postEvent(new QueueTop8Updated(list));
    }

    @Subscribe
    public void onGetQueueAll(GetQueueAllRequest request) {
        List<EpisodeItem> list = queueDao.getAllInQueue(request.getUserDataRequest());
        postEvent(new QueueAllUpdated(list));
    }
}

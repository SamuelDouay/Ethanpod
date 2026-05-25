package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.request.GetInboxAllRequest;
import fr.github.ethanpod.event.request.GetInboxCountRequest;
import fr.github.ethanpod.event.request.GetInboxTop8Request;
import fr.github.ethanpod.event.updated.InboxAllUpdated;
import fr.github.ethanpod.event.updated.InboxCountUpdated;
import fr.github.ethanpod.event.updated.InboxTop8Updated;
import fr.github.ethanpod.logic.sql.dao.InboxDao;

import java.util.List;

public class InboxRequestHandler extends BaseRequestHandler {
    private final InboxDao inboxDao;

    public InboxRequestHandler(InboxDao inboxDao) {
        super();
        this.inboxDao = inboxDao;
    }

    @Subscribe
    public void onGetInboxCount(GetInboxCountRequest request) {
        int count = inboxDao.getNumberOfInbox();
        postEvent(new InboxCountUpdated(count));
    }

    @Subscribe
    public void onGetInboxTop8(GetInboxTop8Request request) {
        List<EpisodeItem> list = inboxDao.getTop8InInbox();
        postEvent(new InboxTop8Updated(list));
    }

    @Subscribe
    public void onGetInboxAll(GetInboxAllRequest request) {
        List<EpisodeItem> list = inboxDao.getAllInInbox(request.getUserDataRequest());
        postEvent(new InboxAllUpdated(list));
    }
}
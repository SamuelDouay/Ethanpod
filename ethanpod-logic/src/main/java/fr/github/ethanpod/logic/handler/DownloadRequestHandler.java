package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.request.GetDownloadAllRequest;
import fr.github.ethanpod.event.request.GetDownloadTop8Request;
import fr.github.ethanpod.event.updated.DownloadAllUpdated;
import fr.github.ethanpod.event.updated.DownloadTop8Updated;
import fr.github.ethanpod.logic.sql.dao.DownloadDao;

import java.util.List;

public class DownloadRequestHandler extends BaseRequestHandler {
    private final DownloadDao downloadDao;

    public DownloadRequestHandler(DownloadDao downloadDao) {
        super();
        this.downloadDao = downloadDao;
    }

    @Subscribe
    public void onGetDownloadTop8(GetDownloadTop8Request request) {
        List<EpisodeItem> list = downloadDao.getTop8Download();
        postEvent(new DownloadTop8Updated(list));
    }

    @Subscribe
    public void onGetDownloadAll(GetDownloadAllRequest request) {
        List<EpisodeItem> list = downloadDao.getAllDownload(request.getUserDataRequest());
        postEvent(new DownloadAllUpdated(list));
    }
}

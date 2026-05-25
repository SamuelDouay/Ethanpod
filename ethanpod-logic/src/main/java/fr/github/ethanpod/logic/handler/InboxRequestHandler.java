package fr.github.ethanpod.logic.handler;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.request.GetInboxAllRequest;
import fr.github.ethanpod.event.request.GetInboxCountRequest;
import fr.github.ethanpod.event.request.GetInboxTop8Request;
import fr.github.ethanpod.event.updated.InboxAllUpdated;
import fr.github.ethanpod.event.updated.InboxCountUpdated;
import fr.github.ethanpod.event.updated.InboxTop8Updated;
import fr.github.ethanpod.logic.sql.dao.InboxDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class InboxRequestHandler {
    private static final Logger logger = LogManager.getLogger(InboxRequestHandler.class);
    private final InboxDao inboxDao;

    public InboxRequestHandler(InboxDao inboxDao) {
        this.inboxDao = inboxDao;
        // S'enregistre automatiquement sur le bus global
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetInboxCount(GetInboxCountRequest request) {
        logger.debug("Traitement de la demande de compteur d'inbox");
        try {
            int count = inboxDao.getNumberOfInbox();
            GlobalEventBus.getInstance().post(new InboxCountUpdated(count));
            logger.debug("Inbox count = {}, événement posté", count);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du compteur d'inbox", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetInboxTop8(GetInboxTop8Request request) {
        logger.debug("Traitement de la demande du top 8 d'inbox");
        try {
            List<EpisodeItem> list = inboxDao.getTop8InInbox();
            GlobalEventBus.getInstance().post(new InboxTop8Updated(list));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du top 8", e);
            // On peut poster un événement d'erreur si nécessaire
        }
    }

    @Subscribe
    public void onGetInboxAll(GetInboxAllRequest request) {
        try {
            List<EpisodeItem> list = inboxDao.getAllInInbox(request.getUserDataRequest());
            GlobalEventBus.getInstance().post(new InboxAllUpdated(list));
        } catch (Exception e) {
            // On peut poster un événement d'erreur si nécessaire
        }
    }
}
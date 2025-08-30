package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.UserDataRequest;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.service.AsyncServiceManager;

public class InboxController extends Controller {

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }


    public void loadInboxCount(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du nombre d'éléments inbox",
                () -> asyncServiceManager.getInboxService().getInboxCountAsync(message.id()),
                EventType.INBOX_COUNT_UPDATED,
                "Erreur lors du chargement du compte inbox"
        );
    }

    public void loadInboxTop8(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du top 8 Inbox",
                () -> asyncServiceManager.getInboxService().getTop8InInbox(message.id()),
                EventType.INBOX_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 inbox"
        );
    }

    public void loadInboxAll(ThreadMessage message) {
        executeAsyncOperation(
                "Chargement du all Inbox",
                () -> asyncServiceManager.getInboxService().getAllInbox(message.id(), (UserDataRequest) message.data()),
                EventType.EPISODE_ALL_UPDATED,
                "Erreur lors du chargement all inbox"
        );
    }
}

package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

public class InboxController extends Controller {

    public InboxController(AsyncServiceManager asyncServiceManager) {
        super(asyncServiceManager);
    }


    public void loadInboxCount() {
        executeAsyncOperation(
                "Chargement du nombre d'éléments inbox",
                () -> asyncServiceManager.getInboxService().getInboxCountAsync(),
                EventType.INBOX_COUNT_UPDATED,
                "Erreur lors du chargement du compte inbox"
        );
    }

    public void loadInboxTop8() {
        executeAsyncOperation(
                "Chargement du top 8 Inbox",
                () -> asyncServiceManager.getInboxService().getTop8InInbox(),
                EventType.INBOX_TOP8_UPDATED,
                "Erreur lors du chargement du top 8 inbox"
        );
    }

    public void loadInboxAll() {
        executeAsyncOperation(
                "Chargement du all Inbox",
                () -> asyncServiceManager.getInboxService().getAllInbox(),
                EventType.INBOX_ALL_UPDATED,
                "Erreur lors du chargement all inbox"
        );
    }
}

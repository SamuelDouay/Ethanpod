package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.service.AsyncServiceManager;

import java.util.concurrent.CompletableFuture;

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


    @Override
    void initializeUI() {
        CompletableFuture.runAsync(this::loadInboxTop8, UI_EXECUTOR).whenComplete((_, _) -> UI_EXECUTOR.shutdown());
        CompletableFuture.runAsync(this::loadInboxCount, UI_EXECUTOR).whenComplete((_, _) -> UI_EXECUTOR.shutdown());
    }
}

package fr.github.ethanpod.view.page;

import com.google.common.eventbus.Subscribe;
import fr.github.ethanpod.core.item.Item;
import fr.github.ethanpod.event.GlobalEventBus;
import fr.github.ethanpod.event.updated.*;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PageEventHandler {
    private static final Logger LOGGER = LogManager.getLogger(PageEventHandler.class);
    private static final int PAGE_SIZE = 100;

    private final PageContentRenderer contentRenderer;
    private final PageDataLoader dataLoader;

    public PageEventHandler(
            PageContentRenderer contentRenderer, PageDataLoader dataLoader) {
        this.contentRenderer = contentRenderer;
        this.dataLoader = dataLoader;
        GlobalEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onSurpriseUpdated(SurpriseAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateSection(event.getItems(), "Get surprises", "SURPRISE"));
    }

    /*
    @Subscribe
    public void onEpisodesByPodcastIdUpdated(PodcastByIdUpdated event, Consumer<String> titleSetter) {
        PodcastFindByIdUpdate podcastEvent = (PodcastFindByIdUpdate) event;
        titleSetter.accept(podcastEvent.getPodcastItem().getTitle());
        contentRenderer.updatePodcastTitle(podcastEvent);
        contentRenderer.updateEpisodes(event.getItems(), !dataLoader.isFirstPage());
    }
*/
    private void handleNavigationUpdate(List<? extends Item> items) {
        checkForMoreData(items);
        contentRenderer.updateSubscriptions(items, !dataLoader.isFirstPage());
    }

    private void checkForMoreData(List<?> items) {
        if (items.isEmpty() || items.size() < PAGE_SIZE) {
            dataLoader.setHasMoreData(false);
            LOGGER.debug("Plus de données disponibles - reçu {} éléments", items.size());
        }
    }

    private void handleAllUpdated(List<? extends Item> items) {
        checkForMoreData(items);
        Platform.runLater(() -> contentRenderer.updateEpisodes(items, !dataLoader.isFirstPage()));
    }

    private void handleTop8Updated(List<? extends Item> items, String title, String section) {
        Platform.runLater(() -> contentRenderer.updateSection(items, title, section));
    }

    @Subscribe
    public void onSubscriptionAllUpdated(SubscriptionAllUpdated event) {
        Platform.runLater(() -> handleNavigationUpdate(event.getItems()));
    }

    @Subscribe
    public void onInboxTop8Updated(InboxTop8Updated e) {
        handleTop8Updated(e.getItems(), "See what's news", "INBOX");
    }

    @Subscribe
    public void onDownloadTop8Updated(DownloadTop8Updated e) {
        handleTop8Updated(e.getItems(), "Manage downloads", "DOWNLOAD");
    }

    @Subscribe
    public void onQueueTop8Updated(QueueTop8Updated e) {
        handleTop8Updated(e.getItems(), "Continue listening", "QUEUE");
    }

    @Subscribe
    public void onPodcastTop8Updated(PodcastReadTop8Updated e) {
        handleTop8Updated(e.getItems(), "Check your classic", "PODCAST");
    }

    @Subscribe
    public void onSurpriseAllUpdated(SurpriseAllUpdated e) {
        handleTop8Updated(e.getItems(), "Get surprises", "SURPRISE");
    }

    @Subscribe
    public void onInboxAllUpdated(InboxAllUpdated e) {
        handleAllUpdated(e.getItems());
    }

    @Subscribe
    public void onDownloadAllUpdated(DownloadAllUpdated e) {
        handleAllUpdated(e.getItems());
    }

    @Subscribe
    public void onQueueAllUpdated(QueueAllUpdated e) {
        handleAllUpdated(e.getItems());
    }

    @Subscribe
    public void onEpisodesAllUpdated(EpisodeAllUpdated e) {
        handleAllUpdated(e.getItems());
    }

    @Subscribe
    public void onHistoryAllUpdated(HistoryAllUpdated e) {
        handleAllUpdated(e.getItems());
    }

    // Nettoyage
    public void dispose() {
        GlobalEventBus.getInstance().unregister(this);
    }
}

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

    private void handleEpisodeUpdate(List<Item> episodes) {
        checkForMoreData(episodes);
        contentRenderer.updateEpisodes(episodes, !dataLoader.isFirstPage());
    }

    @Subscribe
    public void onSurpriseUpdated(SurpriseAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateSection(event.getItems(), "Get surprises", "SURPRISE"));
    }

    @Subscribe
    public void onInboxTop8Updated(InboxTop8Updated event) {
        Platform.runLater(() -> contentRenderer.updateSection(event.getItems(), "See what's news", "INBOX"));
    }

    @Subscribe
    public void onPodcastReadTop8Updated(PodcastReadTop8Updated event) {
        Platform.runLater(() -> contentRenderer.updateSection(event.getItems(), "Check your classic", "PODCAST"));
    }

    @Subscribe
    public void onQueueTop8Updated(QueueTop8Updated event) {
        Platform.runLater(() -> contentRenderer.updateSection(event.getItems(), "Continue listening", "QUEUE"));
    }

    @Subscribe
    public void onDownloadTop8Updated(DownloadTop8Updated event) {
        Platform.runLater(() -> contentRenderer.updateSection(event.getItems(), "Manage downloads", "DOWNLOAD"));
    }

    @Subscribe
    public void onDownloadAllUpdated(DownloadAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateEpisodes(event.getItems(), !dataLoader.isFirstPage()));
    }

    @Subscribe
    public void onEpisodesAllUpdated(EpisodeAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateEpisodes(event.getItems(), !dataLoader.isFirstPage()));
    }

    @Subscribe
    public void onSubscriptionAllUpdated(SubscriptionAllUpdated event) {
        Platform.runLater(() -> handleNavigationUpdate(event.getItems()));
    }

    @Subscribe
    public void onInboxAllUpdated(InboxAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateEpisodes(event.getItems(), !dataLoader.isFirstPage()));
    }

    @Subscribe
    public void onHistoryAllUpdated(HistoryAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateEpisodes(event.getItems(), !dataLoader.isFirstPage()));
    }

    @Subscribe
    public void onQueueAllUpdated(QueueAllUpdated event) {
        Platform.runLater(() -> contentRenderer.updateEpisodes(event.getItems(), !dataLoader.isFirstPage()));
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
    private void handleNavigationUpdate(List<Item> items) {
        checkForMoreData(items);
        contentRenderer.updateSubscriptions(items, !dataLoader.isFirstPage());
    }

    private void checkForMoreData(List<?> items) {
        if (items.isEmpty() || items.size() < PAGE_SIZE) {
            dataLoader.setHasMoreData(false);
            LOGGER.debug("Plus de données disponibles - reçu {} éléments", items.size());
        }
    }

    // Nettoyage
    public void dispose() {
        GlobalEventBus.getInstance().unregister(this);
    }
}

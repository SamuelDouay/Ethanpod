package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.item.Item;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.event.PodcastFindByIdUpdate;
import fr.github.ethanpod.event.UIEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class PageEventHandler {
    private static final Logger LOGGER = LogManager.getLogger(PageEventHandler.class);
    private static final int PAGE_SIZE = 100;

    private final PageContentRenderer contentRenderer;
    private final PageDataLoader dataLoader;

    public PageEventHandler(UIEventManager eventManager, Consumer<String> titleSetter,
                            PageContentRenderer contentRenderer, PageDataLoader dataLoader) {
        this.contentRenderer = contentRenderer;
        this.dataLoader = dataLoader;
        registerEventHandlers(eventManager, titleSetter);
    }

    private void registerEventHandlers(UIEventManager eventManager, Consumer<String> titleSetter) {
        eventManager.registerHandler(EventType.PODCAST_BY_ID_UPDATED, event -> {
            PodcastFindByIdUpdate podcastEvent = (PodcastFindByIdUpdate) event;
            titleSetter.accept(podcastEvent.getPodcastItem().getTitle());
            contentRenderer.updatePodcastTitle(podcastEvent);
        });

        eventManager.registerHandler(EventType.EPISODE_ALL_UPDATED,
                event -> handleEpisodeUpdate(event.getItems()));

        eventManager.registerHandler(EventType.SUBSCRIPTION_ALL_UPDATED,
                event -> handleNavigationUpdate(event.getItems()));

        eventManager.registerHandler(EventType.QUEUE_TOP8_UPDATED,
                event -> contentRenderer.updateSection(event.getItems(), "Continue listening", "QUEUE"));
        eventManager.registerHandler(EventType.INBOX_TOP8_UPDATED,
                event -> contentRenderer.updateSection(event.getItems(), "See what's news", "INBOX"));
        eventManager.registerHandler(EventType.PODCAST_TOP8_UPDATED,
                event -> contentRenderer.updateSection(event.getItems(), "Check your classic", "PODCAST"));
        eventManager.registerHandler(EventType.DOWNLOAD_TOP8_UPDATED,
                event -> contentRenderer.updateSection(event.getItems(), "Manage downloads", "DOWNLOAD"));
        eventManager.registerHandler(EventType.SURPRISE_ALL_UPDATED,
                event -> contentRenderer.updateSection(event.getItems(), "Get surprises", "SURPRISE"));
    }

    private void handleEpisodeUpdate(List<Item> episodes) {
        checkForMoreData(episodes);
        contentRenderer.updateEpisodes(episodes, !dataLoader.isFirstPage());
    }

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
}

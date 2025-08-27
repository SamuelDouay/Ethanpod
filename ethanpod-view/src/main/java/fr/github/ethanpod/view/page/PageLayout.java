package fr.github.ethanpod.view.page;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.event.*;
import fr.github.ethanpod.view.component.EpisodeComponent;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.LayoutContext;
import fr.github.ethanpod.view.context.PageContext;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PageLayout extends Layout implements ContextualLayout {
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private static final String DEFAULT_TITLE = "PAGE";
    private static final Logger log = LogManager.getLogger(PageLayout.class);
    private VBox container;

    public PageLayout(UIEventManager eventManager) {
        super(DEFAULT_TITLE, eventManager);
        registerEventHandlersPodcast(eventManager);
    }

    @Override
    public VBox getLayout() {
        VBox mainBox = getContainer();
        this.container = new VBox();
        mainBox.getChildren().add(getTitle());
        mainBox.getChildren().add(container);
        return mainBox;
    }

    @Override
    public void updateContext(LayoutContext context, LayoutType layoutType) {
        if (context instanceof PageContext(String title, Integer id)) {
            setTitle(title);
            if (id != 0) {
                MessageRouter.getInstance().userRequest(UserRequestType.GET_PODCAST_BY_ID, "[PODCAST]", id);
                MessageRouter.getInstance().userRequest(UserRequestType.GET_EPISODE_BY_PODCAST_ID, "[PODCAST]", id);
            }
            switch (title) {
                case "Queue" -> MessageRouter.getInstance().userRequest(UserRequestType.GET_QUEUE_ALL, "[QUEUE]", null);
                case "Inbox" -> MessageRouter.getInstance().userRequest(UserRequestType.GET_INBOX_ALL, "[INBOX]", null);
                case "Downloads" ->
                        MessageRouter.getInstance().userRequest(UserRequestType.GET_DOWNLOAD_ALL, "[DOWNLOAD]", null);
                default -> {
                }
            }
        }
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return PageContext.class.isAssignableFrom(contextType);
    }

    private void registerEventHandlersPodcast(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.PODCAST_BY_ID_UPDATED,
                this::updatePodcastTitle
        );

        eventManager.registerHandler(EventType.EPISODE_BY_PODCAST_ID_UPDATED,
                (UIEventHandler<EpisodeByPodcastIdUpdatedEvent>) event ->
                        updateEpisode(event.getEpisodeItems())
        );

        eventManager.registerHandler(EventType.QUEUE_ALL_UPDATED,
                (UIEventHandler<QueueAllUpdatedEvent>) event ->
                        updateEpisode(event.getEpisodeItems())
        );

        eventManager.registerHandler(EventType.INBOX_ALL_UPDATED,
                (UIEventHandler<InboxAllUpdatedEvent>) event ->
                        updateEpisode(event.getEpisodeItems())
        );

        eventManager.registerHandler(EventType.DOWNLOAD_ALL_UPDATED,
                (UIEventHandler<DownloadAllUpdatedEvent>) event ->
                        updateEpisode(event.getEpisodeItems())
        );
    }

    private void updatePodcastTitle(PodcastFindByIdUpdate event) {
        log.info("getting podcast: {}", event.getPodcastItem().getTitle());
        setTitle(event.getPodcastItem().getTitle());
        VBox subtitle = new VBox();
        subtitle.getChildren().add(new Label(event.getPodcastItem().getAuthor()));
        subtitle.getChildren().add(new Label(event.getPodcastItem().getDescription()));
        container.getChildren().add(subtitle);
    }

    private void updateEpisode(List<EpisodeItem> episodeItems) {
        log.info("getting {} podcast", episodeItems.size());
        for (EpisodeItem episodeItem : episodeItems) {
            container.getChildren().add(EPISODE_COMPONENT.createEpisode(episodeItem));
        }
    }
}

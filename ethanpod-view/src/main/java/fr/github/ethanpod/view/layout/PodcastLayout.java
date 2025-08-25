package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.event.EpisodeByPodcastIdUpdatedEvent;
import fr.github.ethanpod.event.PodcastFindByIdUpdate;
import fr.github.ethanpod.event.UIEventHandler;
import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.component.EpisodeComponent;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.FeedContext;
import fr.github.ethanpod.view.context.LayoutContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PodcastLayout extends Layout implements ContextualLayout {
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private static final Logger log = LogManager.getLogger(PodcastLayout.class);
    private static final String DEFAULT_TITLE = "Feed";
    private VBox episodeBox;
    private VBox subtitle;

    public PodcastLayout(UIEventManager uiEventManager) {
        super(DEFAULT_TITLE, uiEventManager);
        this.episodeBox = null;
        registerEventHandlers(uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();
        episodeBox = new VBox();
        subtitle = new VBox();

        box.getChildren().add(getTitle());
        box.getChildren().add(subtitle);
        box.getChildren().add(episodeBox);

        return box;
    }

    @Override
    public void updateContext(LayoutContext context) {
        if (context instanceof FeedContext feedContext) {
            MessageRouter.getInstance().userRequest(UserRequestType.GET_PODCAST_BY_ID, "[PODCAST]", feedContext.id());
            MessageRouter.getInstance().userRequest(UserRequestType.GET_EPISODE_BY_PODCAST_ID, "[PODCAST]", feedContext.id());
        }
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return FeedContext.class.isAssignableFrom(contextType);
    }

    private void registerEventHandlers(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.PODCAST_BY_ID_UPDATED,
                (UIEventHandler<PodcastFindByIdUpdate>) event -> {
                    log.info("getting podcast: {}", event.getPodcastItem().getTitle());
                    setTitle(event.getPodcastItem().getTitle());
                    subtitle.getChildren().add(new Label(event.getPodcastItem().getAuthor()));
                    subtitle.getChildren().add(new Label(event.getPodcastItem().getDescription()));
                }
        );

        eventManager.registerHandler(EventType.EPISODE_BY_PODCAST_ID_UPDATED,
                (UIEventHandler<EpisodeByPodcastIdUpdatedEvent>) event -> {
                    log.info("getting {} podcast", event.getEpisodeItems().size());
                    updateEpisode(event.getEpisodeItems());
                }
        );
    }

    private void updateEpisode(List<EpisodeItem> episodeItems) {
        for (EpisodeItem episodeItem : episodeItems) {
            episodeBox.getChildren().add(EPISODE_COMPONENT.createEpisode(episodeItem));
        }
    }

}

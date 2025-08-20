package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.UserRequestType;
import fr.github.ethanpod.event.PodcastFindByIdUpdate;
import fr.github.ethanpod.event.UIEventHandler;
import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.FeedContext;
import fr.github.ethanpod.view.context.LayoutContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeedLayout extends Layout implements ContextualLayout {
    private static final Logger log = LogManager.getLogger(FeedLayout.class);
    private static final String DEFAULT_TITLE = "Feed";
    private VBox box;

    public FeedLayout(UIEventManager uiEventManager) {
        super(DEFAULT_TITLE, uiEventManager);
        registerEventHandlers(uiEventManager);
    }

    @Override
    public VBox getLayout() {
        box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }

    @Override
    public void updateContext(LayoutContext context) {
        if (context instanceof FeedContext feedContext) {
            String newTitle = feedContext.podcastTitle();
            if (feedContext.unreadCount() > 0) {
                newTitle += " (" + feedContext.unreadCount() + ") ";
            }
            MessageRouter.getInstance().userRequest(UserRequestType.GET_FEED_BY_ID, "[FEED]", feedContext.id());
            setTitle(newTitle + feedContext.id());
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
                    box.getChildren().add(new Label(event.getPodcastItem().getAuthor()));
                    box.getChildren().add(new Label(event.getPodcastItem().getDescription()));
                }
        );
    }
}

package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.core.thread.EventType;
import fr.github.ethanpod.event.EpisodeByPodcastIdUpdatedEvent;
import fr.github.ethanpod.event.UIEventHandler;
import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.component.EpisodeComponent;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class QueueLayout extends Layout {
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private static final Logger log = LogManager.getLogger(QueueLayout.class);
    private VBox episodeBox;

    public QueueLayout(UIEventManager uiEventManager) {
        super("Queue", uiEventManager);
        //MessageRouter.getInstance().userRequest(UserRequestType.GET_QUEUE_ALL, "[QUEUE]", null);
        registerEventHandlers(uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();
        this.content = new VBox();

        box.getChildren().add(getTitle());
        box.getChildren().add(content);

        return box;
    }

    private void registerEventHandlers(UIEventManager eventManager) {
        eventManager.registerHandler(EventType.QUEUE_ALL_UPDATED,
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

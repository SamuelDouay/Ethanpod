package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.FeedContext;
import fr.github.ethanpod.view.context.LayoutContext;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

public class FeedLayout extends Layout implements ContextualLayout {
    private static final String DEFAULT_TITLE = "Feed";

    public FeedLayout() {
        super(DEFAULT_TITLE);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }

    @Override
    public void updateContext(LayoutContext context) {
        if (context instanceof FeedContext feedContext) {
            // Assurer que les mises à jour UI se font sur le JavaFX Application Thread
            Platform.runLater(() -> {
                String newTitle = feedContext.podcastTitle();
                if (feedContext.unreadCount() > 0) {
                    newTitle += " (" + feedContext.unreadCount() + ")";
                }
                setTitle(newTitle);
            });
        }
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return FeedContext.class.isAssignableFrom(contextType);
    }
}

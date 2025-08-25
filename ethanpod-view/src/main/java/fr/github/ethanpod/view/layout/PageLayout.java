package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.LayoutContext;
import fr.github.ethanpod.view.context.PageContext;
import javafx.scene.layout.VBox;

public class PageLayout extends Layout implements ContextualLayout {
    private static final String DEFAULT_TITLE = "PAGE";

    protected PageLayout(UIEventManager eventManager) {
        super(DEFAULT_TITLE, eventManager);
    }

    @Override
    public VBox getLayout() {
        VBox container = getContainer();

        container.getChildren().add(getTitle());
        return container;
    }

    @Override
    public void updateContext(LayoutContext context) {
        if (context instanceof PageContext(String title)) {
            this.setTitle(title);
        }
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return PageContext.class.isAssignableFrom(contextType);
    }
}

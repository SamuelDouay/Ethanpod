package fr.github.ethanpod.view.page;

import fr.github.ethanpod.view.context.CleanableLayout;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.LayoutContext;
import fr.github.ethanpod.view.context.PageContext;
import fr.github.ethanpod.view.util.ImageCache;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageLayout extends Layout implements ContextualLayout, CleanableLayout {
    private static final String DEFAULT_TITLE = "PAGE";
    private static final Logger LOGGER = LogManager.getLogger(PageLayout.class);

    private final PageDataLoader dataLoader;
    private final PageContentRenderer contentRenderer;
    private final PageScrollManager scrollManager;
    private final PageEventHandler eventHandler;


    public PageLayout(ScrollPane scrollPane) {
        super(DEFAULT_TITLE, scrollPane);
        this.contentRenderer = new PageContentRenderer(container, grid, this::getGrid);
        this.dataLoader = new PageDataLoader(contentRenderer);
        this.scrollManager = new PageScrollManager(scrollPane, dataLoader);
        this.eventHandler = new PageEventHandler(contentRenderer, dataLoader);
    }

    @Override
    public VBox getLayout() {
        VBox mainBox = getContainer();
        mainBox.getChildren().addAll(getTitle(), container);
        return mainBox;
    }

    @Override
    public void clearContainer() {
        LOGGER.debug("Nettoyage du container PageLayout");
        container.getChildren().clear();
        ImageCache.cleanupDeadReferences();
        dataLoader.resetPaginationState();
        scrollManager.cleanup();
    }

    @Override
    public void updateContext(LayoutContext context, LayoutType layoutType) {
        clearContainer();
        if (context instanceof PageContext(String title, Integer id)) {
            setTitle(title);
            dataLoader.initializeContext(title, id);
            dataLoader.loadDataForCurrentPage();
            scrollManager.setupInfiniteScroll();
        }
    }

    @Override
    public boolean acceptsContext(Class<? extends LayoutContext> contextType) {
        return PageContext.class.isAssignableFrom(contextType);
    }
}
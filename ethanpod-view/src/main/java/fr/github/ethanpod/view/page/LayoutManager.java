package fr.github.ethanpod.view.page;

import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.context.CleanableLayout;
import fr.github.ethanpod.view.context.ContextualLayout;
import fr.github.ethanpod.view.context.LayoutContext;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.scene.control.ScrollPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LayoutManager {
    private static final Logger logger = LogManager.getLogger(LayoutManager.class.getName());
    private final ScrollPane scrollPane;
    private final Map<LayoutType, Layout> layoutCache;
    private final Map<LayoutType, LayoutContext> contextCache;
    private final UIEventManager uiEventManager;

    public LayoutManager(ScrollPane scrollPane, UIEventManager eventManager) {
        this.scrollPane = Objects.requireNonNull(scrollPane, "ScrollPane cannot be null");
        this.layoutCache = new EnumMap<>(LayoutType.class);
        this.contextCache = new ConcurrentHashMap<>();
        this.uiEventManager = eventManager;
        initializeLayouts();
    }

    private void initializeLayouts() {
        layoutCache.put(LayoutType.PAGE, new PageLayout(uiEventManager, scrollPane));
    }

    public void setLayout(LayoutType layoutType) {
        setLayout(layoutType, null);
    }

    public void setLayout(LayoutType layoutType, LayoutContext context) {
        Objects.requireNonNull(layoutType, "LayoutType cannot be null");

        try {
            Layout layout = layoutCache.get(layoutType);

            if (layout instanceof CleanableLayout cleanableLayout) {
                cleanableLayout.clearContainer();
            }

            // Appliquer le contexte si le layout le supporte
            if (context != null && layout instanceof ContextualLayout contextualLayout) {
                if (contextualLayout.acceptsContext(context.getClass())) {
                    contextualLayout.updateContext(context, layoutType);
                    contextCache.put(layoutType, context);
                } else {
                    logger.warn("Layout {} does not accept context of type {}", layoutType, context.getClass().getSimpleName());
                }
            }

            scrollPane.setContent(layout.getLayout());

        } catch (Exception e) {
            logger.warn("Failed to set layout {}: {}", layoutType, e.getMessage());
            // Fallback vers un layout sûr
            if (layoutType != LayoutType.HOME) {
                setLayout(LayoutType.HOME);
            }
        }
    }
}

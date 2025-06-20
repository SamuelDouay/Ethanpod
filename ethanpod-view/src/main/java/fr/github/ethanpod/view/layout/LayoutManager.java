package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.view.layout.context.ContextualLayout;
import fr.github.ethanpod.view.layout.context.LayoutContext;
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
    private LayoutType currentLayoutType;

    public LayoutManager(ScrollPane scrollPane) {
        this.scrollPane = Objects.requireNonNull(scrollPane, "ScrollPane cannot be null");
        this.layoutCache = new EnumMap<>(LayoutType.class);
        this.contextCache = new ConcurrentHashMap<>();
        initializeLayouts();
    }

    private void initializeLayouts() {
        // Initialisation paresseuse des layouts
        for (LayoutType type : LayoutType.values()) {
            layoutCache.put(type, createLayoutInstance(type));
        }
    }

    private Layout createLayoutInstance(LayoutType type) {
        return switch (type) {
            case HOME -> new HomeLayout();
            case QUEUE -> new QueueLayout();
            case INBOX -> new InboxLayout();
            case EPISODES -> new EpisodesLayout();
            case SUBSCRIPTION -> new SubscriptionLayout();
            case DOWNLOAD -> new DownloadLayout();
            case HISTORY -> new HistoryLayout();
            case ADD -> new AddLayout();
            case FEED -> new FeedLayout();
        };
    }

    public void setLayout(LayoutType layoutType) {
        setLayout(layoutType, null);
    }

    public void setLayout(LayoutType layoutType, LayoutContext context) {
        Objects.requireNonNull(layoutType, "LayoutType cannot be null");

        try {
            Layout layout = layoutCache.get(layoutType);

            // Appliquer le contexte si le layout le supporte
            if (context != null && layout instanceof ContextualLayout contextualLayout) {
                if (contextualLayout.acceptsContext(context.getClass())) {
                    contextualLayout.updateContext(context);
                    contextCache.put(layoutType, context);
                } else {
                    logger.warn("Layout {} does not accept context of type {}", layoutType, context.getClass().getSimpleName());
                }
            }

            scrollPane.setContent(layout.getLayout());
            currentLayoutType = layoutType;

        } catch (Exception e) {
            logger.warn("Failed to set layout {}: {}", layoutType, e.getMessage());
            // Fallback vers un layout sûr
            if (layoutType != LayoutType.HOME) {
                setLayout(LayoutType.HOME);
            }
        }
    }

    public LayoutType getCurrentLayoutType() {
        return currentLayoutType;
    }

    public LayoutContext getCurrentContext() {
        return currentLayoutType != null ? contextCache.get(currentLayoutType) : null;
    }

    // Méthode pour rafraîchir le layout actuel avec son contexte
    public void refreshCurrentLayout() {
        if (currentLayoutType != null) {
            LayoutContext context = contextCache.get(currentLayoutType);
            setLayout(currentLayoutType, context);
        }
    }
}

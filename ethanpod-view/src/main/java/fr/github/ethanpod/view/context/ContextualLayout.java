package fr.github.ethanpod.view.context;

import fr.github.ethanpod.view.util.LayoutType;

public interface ContextualLayout {
    void updateContext(LayoutContext context, LayoutType layoutType);

    boolean acceptsContext(Class<? extends LayoutContext> contextType);
}

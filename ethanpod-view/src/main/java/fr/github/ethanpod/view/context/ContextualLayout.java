package fr.github.ethanpod.view.context;

public interface ContextualLayout {
    void updateContext(LayoutContext context);

    boolean acceptsContext(Class<? extends LayoutContext> contextType);
}

package fr.github.ethanpod.view.page;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;

public class PageScrollManager {
    private static final double SCROLL_THRESHOLD = 0.9;

    private final ScrollPane scrollPane;
    private final PageDataLoader dataLoader;
    private ChangeListener<Number> scrollListener;

    public PageScrollManager(ScrollPane scrollPane, PageDataLoader dataLoader) {
        this.scrollPane = scrollPane;
        this.dataLoader = dataLoader;
    }

    public void setupInfiniteScroll() {
        cleanup(); // Nettoie l'ancien listener s'il existe

        scrollListener = (observable, oldValue, newValue) -> {
            if (newValue.doubleValue() >= SCROLL_THRESHOLD && dataLoader.hasMoreData()) {
                dataLoader.loadNextPage();
            }
        };
        scrollPane.vvalueProperty().addListener(scrollListener);
    }

    public void cleanup() {
        if (scrollListener != null) {
            scrollPane.vvalueProperty().removeListener(scrollListener);
            scrollListener = null;
        }
    }
}

package fr.github.ethanpod.view.util;

public class PaginationState {
    private static final int INITIAL_PAGE = 0;
    private int currentPage = INITIAL_PAGE;
    private boolean hasMoreData = true;
    private String currentPageType = "";
    private Integer currentPodcastId = null;

    public void reset() {
        reset("", null);
    }

    public void reset(String pageType, Integer podcastId) {
        this.currentPage = INITIAL_PAGE;
        this.hasMoreData = true;
        this.currentPageType = pageType;
        this.currentPodcastId = podcastId;
    }

    public void nextPage() {
        currentPage++;
    }

    public boolean isFirstPage() {
        return currentPage == INITIAL_PAGE;
    }

    public boolean hasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    public boolean hasPodcastId() {
        return currentPodcastId != null && currentPodcastId != 0;
    }

    // Getters
    public int getCurrentPage() {
        return currentPage;
    }

    public String getCurrentPageType() {
        return currentPageType;
    }

    public Integer getCurrentPodcastId() {
        return currentPodcastId;
    }
}

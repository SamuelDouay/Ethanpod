package fr.github.ethanpod.core.item;

public class SurpriseItem extends Item {
    private final String title;
    private final String podcastTitle;
    private final String imageUrl;

    public SurpriseItem(String title, String podcastTitle, String imageUrl) {
        this.title = title;
        this.podcastTitle = podcastTitle;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPodcastTitle() {
        return podcastTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

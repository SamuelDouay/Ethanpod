package fr.github.ethanpod.core.item;

public class PodcastItem extends Item {
    private final String title;
    private final String description;
    private final String author;
    private final String urlImage;

    public PodcastItem(String title, String description, String author, String urlImage) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrlImage() {
        return urlImage;
    }

    @Override
    public String toString() {
        return "PodcastItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}

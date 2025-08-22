package fr.github.ethanpod.core.item;

public class EpisodeItem extends Item {
    private final String title;
    private final String date;
    private final boolean read;
    private final String description;
    private final String urlImage;
    private final String duration;
    private final String size;
    private final boolean inQueue;
    private final boolean inInbox;
    private final boolean favorite;

    public EpisodeItem(String title, String date, boolean read, String description, String urlImage, String duration, String size, boolean inQueue, boolean inInbox, boolean favorite) {
        this.title = title;
        this.date = date;
        this.read = read;
        this.description = description;
        this.urlImage = urlImage;
        this.duration = duration;
        this.size = size;
        this.inQueue = inQueue;
        this.inInbox = inInbox;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public boolean isRead() {
        return read;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getDuration() {
        return duration;
    }

    public String getSize() {
        return size;
    }

    public boolean isInInbox() {
        return inInbox;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public boolean isFavorite() {
        return favorite;
    }
}

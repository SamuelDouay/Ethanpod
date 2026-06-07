package fr.github.ethanpod.logic.sql.query;

public class EpisodeByPodcastIdQuery extends SqlQueryBuilder {
    private final int podcastId;
    private final int pageSize;
    private final int currentPage;

    public EpisodeByPodcastIdQuery(int podcastId, int pageSize, int currentPage) {
        this.podcastId = podcastId;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        select(
                "FeedItems.title", "FeedItems.pubDate", "FeedItems.read", "FeedItems.description",
                "FeedItems.image_url as item_image", "FeedMedia.duration", "FeedMedia.filesize",
                "Feeds.image_url as feed_image", "Queue.id as queue", "Favorites.id as favorie"
        )
                .from("FeedItems")
                .innerJoin("FeedMedia", "FeedMedia.feeditem = FeedItems.id")
                .innerJoin("Feeds", "Feeds.id = FeedItems.feed")
                .leftJoin("Queue", "Queue.feeditem = FeedItems.id")
                .leftJoin("Favorites", "Favorites.feeditem = FeedItems.id")
                .where("Feeds.id = ?")
                .orderBy("FeedItems.pubDate DESC")
                .limitOffset();
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{podcastId, pageSize, currentPage};
    }
}
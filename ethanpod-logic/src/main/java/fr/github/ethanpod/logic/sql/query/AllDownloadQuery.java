package fr.github.ethanpod.logic.sql.query;

public class AllDownloadQuery extends SqlQueryBuilder {
    private final int pageSize;
    private final int currentPage;

    public AllDownloadQuery(int pageSize, int currentPage) {
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
                .where("FeedMedia.downloaded != 0")
                .orderBy("FeedMedia.downloaded DESC")
                .limitOffset();
    }

    public Object[] getParameters() {
        return new Object[]{pageSize, currentPage};
    }
}
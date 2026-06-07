package fr.github.ethanpod.logic.sql.query;

public class AllHistoryQuery extends SqlQueryBuilder {
    private final int pageSize;
    private final int currentPage;

    public AllHistoryQuery(int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        select(
                "FeedItems.title AS episodeTitle",
                "FeedItems.pubDate AS pubDate",
                "FeedItems.read AS readStatus",
                "FeedItems.description AS episodeDescription",
                "FeedItems.image_url AS itemImage",
                "FeedMedia.duration AS duration",
                "FeedMedia.filesize AS filesize",
                "Feeds.image_url AS feedImage",
                "Queue.id AS queueId",
                "Favorites.id AS favoriteId"
        )
                .from("FeedItems")
                .innerJoin("FeedMedia", "FeedMedia.feeditem = FeedItems.id")
                .innerJoin("Feeds", "Feeds.id = FeedItems.feed")
                .leftJoin("Queue", "Queue.feeditem = FeedItems.id")
                .leftJoin("Favorites", "Favorites.feeditem = FeedItems.id")
                .orderBy("FeedMedia.playback_completion_date DESC")
                .limitOffset();
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{pageSize, currentPage};
    }
}
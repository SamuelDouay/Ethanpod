package fr.github.ethanpod.logic.sql.query;

public class Top8QueueQuery extends SqlQueryBuilder {
    public Top8QueueQuery() {
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
                .innerJoin("Queue", "Queue.feeditem = FeedItems.id")
                .leftJoin("Favorites", "Favorites.feeditem = FeedItems.id")
                .limitValue(8);
    }
}
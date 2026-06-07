package fr.github.ethanpod.logic.sql.query;

public class Top8PodcastReadQuery extends SqlQueryBuilder {
    public Top8PodcastReadQuery() {
        select(
                "Feeds.title AS podcastTitle",
                "Feeds.image_url AS imageUrl",
                "COUNT(*) AS readCount"
        )
                .from("Feeds")
                .innerJoin("FeedItems", "FeedItems.feed = Feeds.id")
                .where("FeedItems.read = 1")
                .groupBy("Feeds.title")
                .orderBy("readCount DESC")
                .limitValue(8);
    }
}
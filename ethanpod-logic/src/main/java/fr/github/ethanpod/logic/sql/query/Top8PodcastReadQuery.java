package fr.github.ethanpod.logic.sql.query;

public class Top8PodcastReadQuery extends SqlQueryBuilder {
    public Top8PodcastReadQuery() {
        select("Feeds.title", "Feeds.image_url", "count(*) as item_read")
                .from("Feeds")
                .innerJoin("FeedItems", "FeedItems.feed = Feeds.id")
                .where("FeedItems.read = 1")
                .groupBy("Feeds.title")
                .orderBy("item_read DESC")
                .limitValue(8);
    }
}
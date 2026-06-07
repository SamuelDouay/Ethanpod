package fr.github.ethanpod.logic.sql.query;

public class SurpriseListQuery extends SqlQueryBuilder {
    public SurpriseListQuery() {
        select("FeedItems.title", "Feeds.title", "FeedItems.image_url as item_image", "Feeds.image_url as feed_image")
                .from("FeedItems")
                .innerJoin("FeedMedia", "FeedMedia.feeditem = FeedItems.id")
                .innerJoin("Feeds", "Feeds.id = FeedItems.feed")
                .where("FeedItems.read != 1")
                .orderBy("RANDOM()")
                .limitValue(9);
    }
}
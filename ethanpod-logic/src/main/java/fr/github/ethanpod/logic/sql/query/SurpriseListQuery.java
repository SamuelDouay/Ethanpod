package fr.github.ethanpod.logic.sql.query;

public class SurpriseListQuery extends SqlQueryBuilder {
    public SurpriseListQuery() {
        select(
                "FeedItems.title AS episodeTitle",
                "Feeds.title AS podcastTitle",
                "FeedItems.image_url AS itemImage",
                "Feeds.image_url AS feedImage"
        ).from("FeedItems")
                .innerJoin("FeedMedia", "FeedMedia.feeditem = FeedItems.id")
                .innerJoin("Feeds", "Feeds.id = FeedItems.feed")
                .where("FeedItems.read != 1")
                .orderBy("RANDOM()")
                .limitValue(9);
    }
}
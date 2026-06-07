package fr.github.ethanpod.logic.sql.query;

public class PodcastByIdQuery extends SqlQueryBuilder {
    private final int podcastId;

    public PodcastByIdQuery(Integer podcastId) {
        this.podcastId = podcastId;
        select(
                "feed.title AS podcastTitle",
                "feed.author AS author",
                "feed.description AS description",
                "feed.image_url AS imageUrl"
        )
                .from("Feeds feed")
                .where("feed.id = ?");
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{podcastId};
    }
}
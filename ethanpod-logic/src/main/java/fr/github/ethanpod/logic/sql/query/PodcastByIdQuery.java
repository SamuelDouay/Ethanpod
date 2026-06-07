package fr.github.ethanpod.logic.sql.query;

public class PodcastByIdQuery extends SqlQueryBuilder {
    private final int id;

    public PodcastByIdQuery(int id) {
        this.id = id;
        select("feed.title", "feed.author", "feed.description", "feed.image_url")
                .from("Feeds feed")
                .where("feed.id = ?");
    }

    public Object[] getParameters() {
        return new Object[]{id};
    }
}
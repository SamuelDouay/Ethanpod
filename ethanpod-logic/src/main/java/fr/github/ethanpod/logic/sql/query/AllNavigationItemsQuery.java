package fr.github.ethanpod.logic.sql.query;

public class AllNavigationItemsQuery extends SqlQueryBuilder {
    public AllNavigationItemsQuery() {
        select("f.id", "f.title", "f.image_url",
                "(SELECT COUNT(*) FROM FeedItems fi WHERE fi.feed = f.id AND fi.read = -1) as unread_count")
                .from("Feeds f")
                .orderBy("unread_count DESC", "f.title ASC");
    }
}
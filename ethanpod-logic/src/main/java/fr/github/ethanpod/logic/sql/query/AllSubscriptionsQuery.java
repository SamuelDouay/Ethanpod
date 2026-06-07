package fr.github.ethanpod.logic.sql.query;

public class AllSubscriptionsQuery extends SqlQueryBuilder {
    private final int pageSize;
    private final int currentPage;

    public AllSubscriptionsQuery(int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        select(
                "f.id AS podcastId",
                "f.title AS podcastTitle",
                "f.image_url AS imageUrl",
                "(SELECT COUNT(*) FROM FeedItems fi WHERE fi.feed = f.id AND fi.read = -1) AS unreadCount"
        )
                .from("Feeds f")
                .orderBy("unreadCount DESC", "f.title ASC")
                .limitOffset();
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{pageSize, currentPage};
    }
}
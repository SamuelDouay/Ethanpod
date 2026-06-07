package fr.github.ethanpod.logic.sql.query;

public class AllSubscriptionsQuery extends SqlQueryBuilder {
    private final int pageSize;
    private final int currentPage;

    public AllSubscriptionsQuery(int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        select("f.id", "f.title", "f.image_url",
                "(SELECT COUNT(*) FROM FeedItems fi WHERE fi.feed = f.id AND fi.read = -1) as unread_count")
                .from("Feeds f")
                .orderBy("unread_count DESC", "f.title ASC")
                .limitOffset();
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{pageSize, currentPage};
    }
}
package fr.github.ethanpod.logic.sql.query;

public class NumberOfInboxQuery extends SqlQueryBuilder {
    public NumberOfInboxQuery() {
        select("COUNT(*) as unreadCount")
                .from("FeedItems AS items")
                .where("items.read = -1");
    }
}
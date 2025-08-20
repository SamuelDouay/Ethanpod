package fr.github.ethanpod.view.context;

public record FeedContext(String podcastTitle, String podcastId, int unreadCount, Integer id) implements LayoutContext {
}

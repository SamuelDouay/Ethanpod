package fr.github.ethanpod.view.context;

import java.util.List;

public record HomeContext(String userName, List<RecentEpisode> recentEpisodes, List<PodcastSuggestion> suggestions,
                          List<ContinueListening> continueListening, int totalDownloads) implements LayoutContext {
    public HomeContext(String userName,
                       List<RecentEpisode> recentEpisodes,
                       List<PodcastSuggestion> suggestions,
                       List<ContinueListening> continueListening,
                       int totalDownloads) {

        this.userName = userName;
        this.recentEpisodes = recentEpisodes != null ? recentEpisodes : List.of();
        this.suggestions = suggestions != null ? suggestions : List.of();
        this.continueListening = continueListening != null ? continueListening : List.of();
        this.totalDownloads = totalDownloads;
    }

    // Classes internes pour les données
    public record RecentEpisode(String imageUrl, String title, String podcastName, String duration, String date,
                                String size, boolean isPlayed) {
    }

    public record PodcastSuggestion(String imageUrl, String title, String podcastName, String description) {
    }

    public record ContinueListening(String imageUrl, String title, String podcastName, String lastPlayedDate,
                                    double progressPercentage) {
    }
}

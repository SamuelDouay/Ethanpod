package fr.github.ethanpod.view.component.surprise;

import javafx.scene.layout.HBox;

public class SurpriseComponent {
    public SurpriseComponent() {
        //  no parameters
    }

    public HBox createSurprise(String imageUrl, String episodeTitle, String podcastTitle) {
        return new SurpriseBuilder()
                .withImageUrl(imageUrl)
                .withEpisodeTitle(episodeTitle)
                .withPodcastTitle(podcastTitle)
                .build();
    }
}

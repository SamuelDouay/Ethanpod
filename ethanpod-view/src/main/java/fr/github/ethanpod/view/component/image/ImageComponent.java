package fr.github.ethanpod.view.component.image;

import javafx.scene.Node;

public class ImageComponent {

    public ImageComponent() {
        // no parameter
    }

    public Node createImageCard(String imageUrl) {
        return new ImageBuilder().withImage(imageUrl).build();
    }

    public Node createImageCard(String imageUrl, String title, int episodeCount) {
        return new ImageBuilder().withImage(imageUrl).withTitle(title).withEpisodeCount(episodeCount).build();
    }

    public Node createImageCard(String imageUrl, String title, String date) {
        return new ImageBuilder().withImage(imageUrl).withTitle(title).withDate(date).build();

    }
}
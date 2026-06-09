package fr.github.ethanpod.view.component.image;

import javafx.scene.Node;

public class ImageComponent {

    private static ImageComponent INSTANCE;

    private ImageComponent() {
        // no parameter
    }

    public static ImageComponent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageComponent();
        }
        return INSTANCE;
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
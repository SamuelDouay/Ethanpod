package fr.github.ethanpod.view.layout;

import javafx.scene.layout.VBox;

public class EpisodesLayout extends Layout {

    public EpisodesLayout() {
        super("Episodes");
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

package fr.github.ethanpod.view.layout;

import javafx.scene.layout.VBox;

public class QueueLayout extends Layout{

    public QueueLayout() {
        super("Queue");
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

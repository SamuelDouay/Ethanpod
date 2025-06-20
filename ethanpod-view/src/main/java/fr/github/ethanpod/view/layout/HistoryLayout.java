package fr.github.ethanpod.view.layout;

import javafx.scene.layout.VBox;

public class HistoryLayout extends Layout {

    public HistoryLayout() {
        super("History");
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

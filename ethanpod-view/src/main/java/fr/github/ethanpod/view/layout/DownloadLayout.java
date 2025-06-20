package fr.github.ethanpod.view.layout;

import javafx.scene.layout.VBox;

public class DownloadLayout extends Layout {

    public DownloadLayout() {
        super("Download");
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

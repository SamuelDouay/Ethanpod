package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.event.UIEventManager;
import javafx.scene.layout.VBox;

public class DownloadLayout extends Layout {

    public DownloadLayout(UIEventManager uiEventManager) {
        super("Download", uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

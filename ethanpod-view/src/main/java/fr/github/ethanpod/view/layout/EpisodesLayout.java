package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.view.event.UIEventManager;
import javafx.scene.layout.VBox;

public class EpisodesLayout extends Layout {

    public EpisodesLayout(UIEventManager uiEventManager) {
        super("Episodes", uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

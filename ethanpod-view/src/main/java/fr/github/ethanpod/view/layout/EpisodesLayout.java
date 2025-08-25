package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.page.Layout;
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

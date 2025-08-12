package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.event.UIEventManager;
import javafx.scene.layout.VBox;

public class QueueLayout extends Layout {

    public QueueLayout(UIEventManager uiEventManager) {
        super("Queue", uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

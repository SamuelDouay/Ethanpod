package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.view.event.UIEventManager;
import javafx.scene.layout.VBox;

public class AddLayout extends Layout {

    public AddLayout(UIEventManager uiEventManager) {
        super("Add podcast", uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

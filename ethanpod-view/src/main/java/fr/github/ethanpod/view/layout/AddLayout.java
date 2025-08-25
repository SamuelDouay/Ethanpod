package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.page.Layout;
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

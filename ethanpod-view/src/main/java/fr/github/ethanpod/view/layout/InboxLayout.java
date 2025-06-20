package fr.github.ethanpod.view.layout;

import javafx.scene.layout.VBox;

public class InboxLayout extends Layout {

    public InboxLayout() {
        super("Inbox");
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

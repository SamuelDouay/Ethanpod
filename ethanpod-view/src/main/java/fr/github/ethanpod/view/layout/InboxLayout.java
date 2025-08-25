package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.page.Layout;
import javafx.scene.layout.VBox;

public class InboxLayout extends Layout {

    public InboxLayout(UIEventManager uiEventManager) {
        super("Inbox", uiEventManager);
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

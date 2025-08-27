package fr.github.ethanpod.view.page;

import fr.github.ethanpod.event.UIEventManager;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class Layout {
    private static final String FONT_FAMILY = "Inter";
    protected final StringProperty titleProperty;
    protected final UIEventManager eventManager;
    protected final VBox container;

    protected Layout(String initialTitle, UIEventManager eventManager) {
        Platform.runLater(System::gc);
        ImageCache.cleanupDeadReferences();
        this.titleProperty = new SimpleStringProperty(initialTitle);
        this.eventManager = eventManager;
        this.container = new VBox();
    }

    protected Label getTitle() {
        Label label = new Label();
        label.textProperty().bind(titleProperty);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 36));
        label.setTextFill(ColorThemeConstants.getMain950());
        return label;
    }

    protected final void setTitle(String title) {
        titleProperty.set(title);
    }

    protected VBox getContainer() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(32.0, 64.0, 32.0, 64.0));
        box.setSpacing(35.0);
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    public abstract VBox getLayout();
}

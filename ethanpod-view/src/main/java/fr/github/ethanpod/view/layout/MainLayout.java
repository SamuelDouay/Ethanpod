package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.view.component.SearchComponent;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.LayoutType;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class MainLayout {
    private LayoutManager layoutManager;

    public MainLayout() {
        // no parameters
    }

    public AnchorPane createInterface() {
        AnchorPane root = new AnchorPane();

        ScrollPane scrollPane = createMainContainer();
        layoutManager = new LayoutManager(scrollPane);
        layoutManager.setLayout(LayoutType.HOME);

        root.getChildren().addAll(scrollPane, createHeader(), createNavigationMenu(), createFooter());
        root.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));
        return root;
    }

    private VBox createNavigationMenu() {
        VBox menu = new VBox();
        menu.setPrefWidth(240.0);

        menu.getChildren().add(new NavigationContainer(layoutManager).createMenu());
        menu.setBorder(new Border(new BorderStroke(ColorThemeConstants.getMain950(), BorderStrokeStyle.SOLID, null, new BorderWidths(0, 1, 0, 0), null)));
        menu.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain500(), null, null)));

        AnchorPane.setLeftAnchor(menu, 0.0);
        AnchorPane.setTopAnchor(menu, 0.0);
        AnchorPane.setBottomAnchor(menu, 72.0);

        return menu;
    }

    private ScrollPane createMainContainer() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(Background.EMPTY);
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(0);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        AnchorPane.setLeftAnchor(scrollPane, 240.0);
        AnchorPane.setTopAnchor(scrollPane, 72.0);
        AnchorPane.setBottomAnchor(scrollPane, 72.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        return scrollPane;
    }

    private HBox createHeader() {
        HBox box = new HBox();

        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefHeight(72.0);
        box.setPadding(new Insets(12.0, 32.0, 12.0, 32.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain000(), null, null)));
        box.setBorder(new Border(new BorderStroke(ColorThemeConstants.getMain950(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(0, 0, 1, 0), null)));

        box.getChildren().add(SearchComponent.createSearchComponent());

        AnchorPane.setTopAnchor(box, 0.0);
        AnchorPane.setLeftAnchor(box, 240.0);
        AnchorPane.setRightAnchor(box, 0.0);

        return box;
    }

    private HBox createFooter() {
        HBox box = new HBox();

        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefHeight(72.0);
        box.setPadding(new Insets(12.0, 32.0, 12.0, 32.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain000(), null, null)));
        box.setBorder(new Border(new BorderStroke(ColorThemeConstants.getMain950(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1, 0, 0, 0), null)));

        box.getChildren().add(SearchComponent.createSearchComponent());

        AnchorPane.setBottomAnchor(box, 0.0);
        AnchorPane.setLeftAnchor(box, 0.0);
        AnchorPane.setRightAnchor(box, 0.0);

        return box;
    }
}

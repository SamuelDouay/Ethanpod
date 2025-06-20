package fr.github.ethanpod.view.component;

import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignM;

public class SearchComponent {
    private SearchComponent() {

    }

    public static HBox createSearchComponent() {
        HBox box = new HBox();

        FontIcon icon = new FontIcon(MaterialDesignM.MAGNIFY);
        icon.setIconColor(ColorThemeConstants.getGrey800());
        icon.setIconSize(25);

        Label label = new Label("Titre, podcast, auteurs");
        label.setFont(Font.font("Inter", FontWeight.LIGHT, 12));
        label.setTextFill(ColorThemeConstants.getGrey800());

        box.getChildren().addAll(icon, label);

        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10.0, 22.0,10.0,22.0));
        box.setSpacing(12.0);
        box.setPrefWidth(448.0);
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey050(), new CornerRadii(4.0), null)));
        return box;
    }
}

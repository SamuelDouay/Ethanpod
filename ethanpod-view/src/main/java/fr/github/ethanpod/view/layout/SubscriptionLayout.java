package fr.github.ethanpod.view.layout;

import fr.github.ethanpod.view.component.image.ImageComponent;
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SubscriptionLayout extends Layout {

    public SubscriptionLayout() {
        super("Subscription");// no param
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());
        box.getChildren().add(getGrid());

        return box;
    }

    private FlowPane getGrid() {
        FlowPane box = new FlowPane();
        box.setVgap(15.0);
        box.setHgap(15.0);
        box.setPrefWidth(Region.USE_PREF_SIZE);
        box.setMaxHeight(Region.USE_PREF_SIZE);
        box.setOrientation(Orientation.HORIZONTAL);
        ImageComponent factory = new ImageComponent();

        for (int i = 0; i < 15; i++) {

            box.getChildren().add(factory.createImageCard(String.valueOf(SubscriptionLayout.class.getResource("/images/ex.jpeg")), "EX...", 10));
            box.getChildren().add(factory.createImageCard(String.valueOf(SubscriptionLayout.class.getResource("/images/heure_du_monde.png")), "L'heure du monde", 0));
            box.getChildren().add(factory.createImageCard(String.valueOf(SubscriptionLayout.class.getResource("/images/small_talk.jpg")), "Small Talk", 125));
            box.getChildren().add(factory.createImageCard(String.valueOf(SubscriptionLayout.class.getResource("/images/underscore.jpeg")), "Undersore", 25));
            box.getChildren().add(factory.createImageCard(String.valueOf(SubscriptionLayout.class.getResource("/images/zerl.jpg")), "Zack en roue libre", 5));

        }

        return box;
    }
}

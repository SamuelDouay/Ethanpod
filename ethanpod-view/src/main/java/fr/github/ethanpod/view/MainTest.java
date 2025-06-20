package fr.github.ethanpod.view;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.view.component.badge.BadgeComponent;
import fr.github.ethanpod.view.component.button.ButtonComponent;
import fr.github.ethanpod.view.component.episode.EpisodeComponent;
import fr.github.ethanpod.view.component.image.ImageComponent;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import fr.github.ethanpod.view.util.ThemeType;
import fr.github.ethanpod.view.util.TypeButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;

public class MainTest extends Application {
    public static final Logger logger = LogManager.getLogger(MainTest.class);
    public static final String IMAGES_HEURE_DU_MONDE_PNG = "/images/heure_du_monde.png";
    public static final String TITLE_EXAMPLE = "Lil Nas X, une icône noire, et gay et flamboyante [REDIF]";

    private static final ButtonComponent BUTTON_COMPONENT = new ButtonComponent();
    private static final BadgeComponent BADGE_COMPONENT = new BadgeComponent();
    private static final EpisodeComponent EPISODE_COMPONENT = new EpisodeComponent();
    private Scene scene;
    private Label currentSelectionLabel;

    public static void main(String[] args) {
        logger.info("Initialisation de l'interface utilisateur principale (MainTest)");
        launch(args);
        logger.info("Fermeture de l'interface utilisateur principale (MainTest)");
    }

    @Override
    public void start(Stage stage) {
        // Créer l'interface initiale
        AnchorPane root = createInterface();

        // Configurer la scène
        scene = new Scene(root, 320, 240);

        // Ajouter un écouteur pour mettre à jour l'interface lors du changement de thème
        ColorThemeConstants.addThemeChangeListener(_ -> refreshInterface());

        // Finaliser la configuration du stage
        stage.setScene(scene);
        stage.setTitle("AntennaPod");
        stage.setMaximized(true);
        stage.show();
        logger.info("Interface utilisateur initialisée avec succès");
    }

    private void refreshInterface() {
        // Sauvegarder l'état actuel (si nécessaire)
        String currentSelection = currentSelectionLabel.getText();

        // Créer une nouvelle interface
        AnchorPane newRoot = createInterface();
        currentSelectionLabel.setText(currentSelection);

        // Mettre à jour la scène
        scene.setRoot(newRoot);
        logger.info("Interface utilisateur mise à jour avec le thème:  {}", ColorThemeConstants.getCurrentTheme());
    }

    private AnchorPane createInterface() {
        AnchorPane root = new AnchorPane();

        VBox mainContainer = createMainContainer();

        // Ajouter les composants au root
        root.getChildren().addAll(mainContainer);

        root.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getGrey000(), null, null)));

        return root;
    }

    private VBox createMainContainer() {
        // Créer le label de sélection
        currentSelectionLabel = new Label("Sélection actuelle : Accueil");
        currentSelectionLabel.setFont(new Font(36));
        currentSelectionLabel.setTextFill(ColorThemeConstants.getMain950());
        currentSelectionLabel.setPadding(new Insets(10, 0, 10, 0));

        // Créer le bouton de mode
        Button themeToggleBtn = createThemeToggleButton();

        // Créer le contenu principal
        Node mainContent = createMainContent();

        // Assembler dans un conteneur
        VBox mainContainer = new VBox(10);
        mainContainer.getChildren().addAll(currentSelectionLabel, themeToggleBtn, mainContent);
        mainContainer.setPadding(new Insets(10));


        EpisodeItem episodeItem = new EpisodeItem(String.valueOf(MainTest.class.getResource(IMAGES_HEURE_DU_MONDE_PNG)),
                false,
                TITLE_EXAMPLE,
                "00:20:40",
                "28/10/2024",
                "18 Mo",
                true);

        EpisodeItem episodeItem1 = new EpisodeItem(String.valueOf(MainTest.class.getResource(IMAGES_HEURE_DU_MONDE_PNG)),
                true,
                TITLE_EXAMPLE,
                "00:20:40",
                "28/10/2024",
                "18 Mo",
                false);

        mainContainer.getChildren().addAll(EPISODE_COMPONENT.createInboxEpisode(episodeItem), EPISODE_COMPONENT.createInboxEpisode(episodeItem1));

        // Configurer le positionnement
        AnchorPane.setLeftAnchor(mainContainer, 240.0);
        AnchorPane.setTopAnchor(mainContainer, 0.0);
        AnchorPane.setRightAnchor(mainContainer, 0.0);

        return mainContainer;
    }

    private Button createThemeToggleButton() {
        Button btn = BUTTON_COMPONENT.createPrimaryButton("MODE");

        btn.setOnAction(_ -> {
            if (ColorThemeConstants.getCurrentTheme().equals(ThemeType.LIGHT)) {
                ColorThemeConstants.setTheme(ThemeType.DARK);
            } else {
                ColorThemeConstants.setTheme(ThemeType.LIGHT);
            }
        });

        return btn;
    }

    private Node createMainContent() {
        VBox box = new VBox(10.0);
        box.setPadding(new Insets(4.0, 16.0, 4.0, 16.0));

        // Ligne de boutons et badges
        HBox buttonRow = createButtonRow();

        // Ligne de boutons avec icônes
        HBox iconButtonRow = createIconButtonRow();

        // Ligne de cartes de podcast simples
        HBox podcastRow = createPodcastRow();

        // Ligne de cartes de podcast avec descriptions
        HBox podcastWithInfoRow = createPodcastWithInfoRow();

        // Ligne de badges texte
        HBox textBadgeRow = createTextBadgeRow();

        // Ajouter toutes les lignes au conteneur principal
        box.getChildren().addAll(podcastRow, buttonRow, iconButtonRow, podcastWithInfoRow, textBadgeRow);

        return box;
    }

    private HBox createButtonRow() {
        HBox hBox = new HBox(15.0);

        // Ajouter les boutons
        hBox.getChildren().add(BUTTON_COMPONENT.createPrimaryButton(TypeButton.PRIMARY.name()));
        hBox.getChildren().add(BUTTON_COMPONENT.createSecondaryButton(TypeButton.SECONDARY.name()));
        hBox.getChildren().add(BUTTON_COMPONENT.createTertiaryButton(TypeButton.TERTIARY.name()));

        // Ajouter les badges avec icônes
        hBox.getChildren().add(BADGE_COMPONENT.createRedBadge(new FontIcon(MaterialDesignP.PLUS)));
        hBox.getChildren().add(BADGE_COMPONENT.createBlueBadge(new FontIcon(MaterialDesignI.INBOX)));
        hBox.getChildren().add(BADGE_COMPONENT.createGreenBadge(new FontIcon(MaterialDesignI.INBOX)));
        hBox.getChildren().add(BADGE_COMPONENT.createPurpleBadge(new FontIcon(MaterialDesignP.PLAYLIST_PLAY)));

        // Ajouter les badges avec texte et icônes
        hBox.getChildren().add(BADGE_COMPONENT.createRedBadge("PLUS", new FontIcon(MaterialDesignP.PLUS)));
        hBox.getChildren().add(BADGE_COMPONENT.createBlueBadge("MAIL", new FontIcon(MaterialDesignI.INBOX)));
        hBox.getChildren().add(BADGE_COMPONENT.createGreenBadge("MAIL", new FontIcon(MaterialDesignI.INBOX)));
        hBox.getChildren().add(BADGE_COMPONENT.createPurpleBadge("PLAY", new FontIcon(MaterialDesignP.PLAYLIST_PLAY)));

        return hBox;
    }

    private HBox createIconButtonRow() {
        HBox hBox = new HBox(15.0);

        // Ajouter les boutons avec icônes
        hBox.getChildren().add(BUTTON_COMPONENT.createPrimaryButton(new FontIcon(MaterialDesignP.PLAY)));
        hBox.getChildren().add(BUTTON_COMPONENT.createSecondaryButton(new FontIcon(MaterialDesignP.PLAY)));
        hBox.getChildren().add(BUTTON_COMPONENT.createTertiaryButton(new FontIcon(MaterialDesignP.PLAY)));

        // Ajouter les boutons avec texte et icônes
        hBox.getChildren().add(BUTTON_COMPONENT.createPrimaryButton("PLAY", new FontIcon(MaterialDesignP.PLAY)));
        hBox.getChildren().add(BUTTON_COMPONENT.createSecondaryButton("PLAY", new FontIcon(MaterialDesignP.PLAY)));
        hBox.getChildren().add(BUTTON_COMPONENT.createTertiaryButton("PLAY", new FontIcon(MaterialDesignP.PLAY)));

        return hBox;
    }

    private HBox createPodcastRow() {
        HBox hBox = new HBox(15.0);
        ImageComponent factory = new ImageComponent();

        // Ajouter les cartes de podcast
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/ex.jpeg"))));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource(IMAGES_HEURE_DU_MONDE_PNG))));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/small_talk.jpg"))));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/underscore.jpeg"))));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/zerl.jpg"))));

        return hBox;
    }

    private HBox createPodcastWithInfoRow() {
        HBox hBox = new HBox(15.0);
        ImageComponent factory = new ImageComponent();

        // Ajouter les cartes de podcast avec titre et nombre d'épisodes
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/ex.jpeg")), "EX...", 10));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource(IMAGES_HEURE_DU_MONDE_PNG)), "L'heure du monde", 0));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/small_talk.jpg")), "Small Talk", 125));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/underscore.jpeg")), "Undersore", 25));
        hBox.getChildren().add(factory.createImageCard(String.valueOf(MainTest.class.getResource("/images/zerl.jpg")), "Zack en roue libre", 5));

        return hBox;
    }

    private HBox createTextBadgeRow() {
        HBox hBox = new HBox(15.0);

        // Ajouter les badges texte
        hBox.getChildren().add(BADGE_COMPONENT.createGreenBadge("Download"));
        hBox.getChildren().add(BADGE_COMPONENT.createRedBadge("Sans media"));
        hBox.getChildren().add(BADGE_COMPONENT.createBlueBadge("Téléchargé"));
        hBox.getChildren().add(BADGE_COMPONENT.createPurpleBadge("Téléchargé"));

        return hBox;
    }
}

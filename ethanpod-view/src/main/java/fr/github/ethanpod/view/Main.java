package fr.github.ethanpod.view;

import fr.github.ethanpod.view.layout.MainLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Initialisation de l'interface utilisateur principale (Main)");
        launch(args);
        logger.info("Fermeture de l'interface utilisateur principale (Main)");
    }

    @Override
    public void start(Stage stage) {
        MainLayout mainLayout = new MainLayout();
        AnchorPane root = mainLayout.createInterface();
        Scene scene = new Scene(root, 320, 240);

        stage.setScene(scene);
        stage.setTitle("AntennaPod");
        stage.setMaximized(true);
        stage.show();
        logger.info("Interface utilisateur initialisée avec succès");
    }
}
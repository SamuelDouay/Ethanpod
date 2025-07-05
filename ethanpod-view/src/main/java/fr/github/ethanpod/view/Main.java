package fr.github.ethanpod.view;

import fr.github.ethanpod.view.layout.MainLayout;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Initialisation de l'interface utilisateur principale (Main)");

        // Configurer le comportement de fermeture de JavaFX
        Platform.setImplicitExit(false);

        try {
            launch(args);
        } catch (Exception e) {
            logger.error("Erreur lors du lancement de JavaFX", e);
            throw e;
        } finally {
            logger.info("Fermeture de l'interface utilisateur principale (Main)");
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            logger.info("Démarrage de l'interface utilisateur JavaFX");

            // Créer le layout principal
            AnchorPane root = new MainLayout().createInterface();

            // Créer la scène
            Scene scene = new Scene(root, 320, 240);

            // Configurer la fenêtre
            stage.setScene(scene);
            stage.setTitle("AntennaPod");
            stage.setMaximized(true);

            // Gérer la fermeture de la fenêtre
            stage.setOnCloseRequest(_ -> {
                logger.info("Demande de fermeture de la fenêtre principale");
                handleApplicationShutdown();
            });

            // Afficher la fenêtre
            stage.show();
            logger.info("Interface utilisateur initialisée avec succès");

            // Notifier le ViewThread que JavaFX est prêt
            notifyViewThreadReady();

        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation de l'interface", e);
            handleApplicationShutdown();
            throw e;
        }
    }

    private void notifyViewThreadReady() {
        try {
            ViewThread viewThread = ViewThread.getInstance();
            if (viewThread != null) {
                viewThread.onJavaFXReady();
                logger.info("ViewThread notifié que JavaFX est prêt");
            } else {
                logger.warn("ViewThread non disponible lors de la notification JavaFX");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la notification du ViewThread", e);
        }
    }

    private void handleApplicationShutdown() {
        try {
            logger.info("Début de l'arrêt de l'application JavaFX");
            Platform.exit();

        } catch (Exception e) {
            logger.error("Erreur lors de l'arrêt de l'application", e);
        }
    }

    @Override
    public void stop() throws Exception {
        logger.info("Méthode stop() de JavaFX appelée");
        super.stop();
    }
}
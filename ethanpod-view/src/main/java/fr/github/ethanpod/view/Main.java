package fr.github.ethanpod.view;

import fr.github.ethanpod.logic.handler.HandlerInitializer;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.util.setting.ConfigProperties;
import fr.github.ethanpod.view.page.MainLayout;
import fr.github.ethanpod.view.util.ImageCache;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    public static final ConfigProperties CONFIG_PROPERTIES = ConfigProperties.getInstance();
    public static final Logger logger = LogManager.getLogger(Main.class);
    private LocalDateTime startTime;

    public static void main(String[] args) {
        logger.info("Initialisation de l'interface utilisateur principale (Main)");

        System.setProperty("javafx.animation.pulse", CONFIG_PROPERTIES.getProperty("javafx.animation.pulse"));
        System.setProperty("javafx.embed.singleThread", CONFIG_PROPERTIES.getProperty("javafx.embed.singleThread"));
        System.setProperty("prism.order", CONFIG_PROPERTIES.getProperty("prism.order"));
        System.setProperty("prism.text", CONFIG_PROPERTIES.getProperty("prism.text"));
        System.setProperty("prism.subpixeltext", CONFIG_PROPERTIES.getProperty("prism.subpixeltext"));
        System.setProperty("prism.lcdtext", CONFIG_PROPERTIES.getProperty("prism.lcdtext"));

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
        // Horodatage du démarrage
        startTime = LocalDateTime.now();
        logStartup(startTime);

        try {
            // Initialisation de la base et des handlers
            initializeSystem();

            logger.debug("Démarrage de l'interface utilisateur JavaFX");

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
                logger.debug("Demande de fermeture de la fenêtre principale");
                handleApplicationShutdown();
            });

            // Afficher la fenêtre
            stage.show();
            logger.debug("Interface utilisateur initialisée avec succès");

            // Nettoyage
            Platform.runLater(System::gc);

        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation de l'interface", e);
            handleApplicationShutdown();
            throw e;
        }
    }

    private void initializeSystem() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.initialize();

        HandlerInitializer.initializeAll(databaseManager);

        logger.info("Système initialisé avec succès");
    }

    private void handleApplicationShutdown() {
        try {
            logger.debug("Début de l'arrêt de l'application JavaFX");
            Platform.exit();
        } catch (Exception e) {
            logger.error("Erreur lors de l'arrêt de l'application", e);
        }
    }

    @Override
    public void stop() throws Exception {
        logger.debug("Méthode stop() de JavaFX appelée");
        ImageCache.shutdown();
        logShutdown();
        super.stop();
    }

    private void logStartup(LocalDateTime startTime) {
        String date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(startTime);
        logger.info("=== Démarrage de l'application Ethanpod ===");
        logger.info("Heure de démarrage: {}", date);
    }

    private void logShutdown() {
        logger.info("=== Fermeture de l'application Ethanpod ===");
    }
}
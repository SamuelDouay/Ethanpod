package fr.github.ethanpod.view.page;

import fr.github.ethanpod.view.component.Buttons;
import fr.github.ethanpod.view.component.SearchComponent;
import fr.github.ethanpod.view.util.ColorThemeConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

public class FooterLayout {
    private static final Logger log = LogManager.getLogger(FooterLayout.class);
    private Timeline progressTimeline;
    private MediaPlayer player;
    private ProgressBar progressBar;
    private Slider slider;
    private boolean mediaReady = false;
    private boolean sliderBeingAdjusted = false;

    public FooterLayout() {
        // no parameters
    }

    public HBox createFooter() {
        HBox box = new HBox();
        box.setSpacing(10); // Ajout d'espacement entre les éléments

        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefHeight(72.0);
        box.setPadding(new Insets(12.0, 32.0, 12.0, 32.0));
        box.setBackground(new Background(new BackgroundFill(ColorThemeConstants.getMain000(), null, null)));
        box.setBorder(new Border(new BorderStroke(ColorThemeConstants.getMain950(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1, 0, 0, 0), null)));

        box.getChildren().add(SearchComponent.createSearchComponent());

        // Initialisation du MediaPlayer - Vérification que le fichier existe
        try {
            String mediaUrl = getClass().getResource("/dataView/Paulo.255.mp3").toExternalForm();
            log.info("Loading media from: " + mediaUrl);
            Media media = new Media(mediaUrl);
            player = new MediaPlayer(media);

            // Attendre que le média soit prêt
            player.setOnReady(() -> {
                mediaReady = true;
                log.info("Media ready - Duration: " + player.getTotalDuration().toString());

                // Configuration de la Timeline après que le média soit prêt
                setupProgressTimeline();
            });

            player.setOnError(() -> {
                log.error("MediaPlayer error: " + player.getError().getMessage());
                mediaReady = false;
            });

        } catch (Exception e) {
            log.error("Error loading media file: " + e.getMessage());
            mediaReady = false;
        }

        Button play = Buttons.primaryIcon(new FontIcon(MaterialDesignP.PLAY));
        Button pause = Buttons.primaryIcon(new FontIcon(MaterialDesignP.PAUSE));
        Button stop = Buttons.primaryIcon(new FontIcon(MaterialDesignS.STOP));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        progressBar.setPrefHeight(10);

        slider = new Slider();
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(0);
        slider.setPrefWidth(200);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        // Événement pour le slider - navigation manuelle
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (sliderBeingAdjusted && mediaReady && player != null && player.getTotalDuration() != null) {
                Duration seekTime = player.getTotalDuration().multiply(newValue.doubleValue());
                player.seek(seekTime);
                progressBar.setProgress(newValue.doubleValue());
                log.info("Slider seeking to: " + seekTime.toString() + " (value: " + newValue.doubleValue() + ")");
            }
        });

        // Détecter quand l'utilisateur commence à ajuster le slider
        slider.setOnMousePressed(event -> {
            sliderBeingAdjusted = true;
        });

        // Détecter quand l'utilisateur relâche le slider
        slider.setOnMouseReleased(event -> {
            sliderBeingAdjusted = false;
        });

        box.getChildren().addAll(play, pause, stop, progressBar, slider);

        // Événements des boutons
        play.setOnMouseClicked(_ -> {
            if (mediaReady && player != null) {
                player.play();
                if (progressTimeline != null) {
                    progressTimeline.play();
                }
                log.info("Playing media - Status: " + player.getStatus());
            } else {
                log.warn("Media not ready yet or player is null");
            }
        });

        pause.setOnMouseClicked(_ -> {
            if (player != null) {
                player.pause();
                if (progressTimeline != null) {
                    progressTimeline.pause();
                }
                log.info("Pausing media - Status: " + player.getStatus());
            }
        });

        stop.setOnMouseClicked(_ -> {
            if (player != null) {
                player.stop();
                if (progressTimeline != null) {
                    progressTimeline.stop();
                }
                player.seek(Duration.ZERO);
                progressBar.setProgress(0.0);
                slider.setValue(0.0);
                log.info("Stopping media");
            }
        });

        // Gestion du clic sur la progress bar pour naviguer
        progressBar.setOnMouseClicked(event -> {
            if (mediaReady && player != null && player.getTotalDuration() != null && !player.getTotalDuration().isUnknown()) {
                double mouseX = event.getX();
                double progressBarWidth = progressBar.getWidth();
                double seekRatio = mouseX / progressBarWidth;
                Duration seekTime = player.getTotalDuration().multiply(seekRatio);
                player.seek(seekTime);
                progressBar.setProgress(seekRatio);
                slider.setValue(seekRatio);
                log.info("ProgressBar seeking to: " + seekTime.toString() + " (ratio: " + seekRatio + ")");
            }
        });

        // Événement quand la lecture se termine naturellement
        if (player != null) {
            player.setOnEndOfMedia(() -> {
                if (progressTimeline != null) {
                    progressTimeline.stop();
                }
                progressBar.setProgress(1.0);
                slider.setValue(1.0);
                log.info("End of media reached");
            });
        }

        return box;
    }

    private void setupProgressTimeline() {
        progressTimeline = new Timeline(
                new KeyFrame(Duration.millis(250), _ -> {
                    updateProgress();
                })
        );
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateProgress() {
        if (player != null &&
                player.getStatus() == MediaPlayer.Status.PLAYING &&
                mediaReady &&
                player.getTotalDuration() != null &&
                !player.getTotalDuration().isUnknown()) {

            Duration currentTime = player.getCurrentTime();
            Duration totalDuration = player.getTotalDuration();

            if (currentTime != null && totalDuration.toMillis() > 0) {
                double progress = currentTime.toMillis() / totalDuration.toMillis();
                double clampedProgress = Math.min(Math.max(progress, 0.0), 1.0);


                if (!sliderBeingAdjusted) {
                    progressBar.setProgress(clampedProgress);
                    slider.setValue(clampedProgress);
                }

                log.debug("Media progress: " + String.format("%.2f%%", clampedProgress * 100) +
                        " - Current: " + formatDuration(currentTime) +
                        " / Total: " + formatDuration(totalDuration));
            }
        }
    }

    private String formatDuration(Duration duration) {
        if (duration == null) return "null";
        int minutes = (int) (duration.toMinutes());
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
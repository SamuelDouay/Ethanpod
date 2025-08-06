package fr.github.ethanpod.app;

import fr.github.ethanpod.controller.ViewThread;
import fr.github.ethanpod.logic.LogicThread;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import fr.github.ethanpod.view.controller.UIEventThread;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final AtomicBoolean isRunning = new AtomicBoolean(true);
    private ExecutorService logicExecutor;
    private ExecutorService viewExecutor;
    private ExecutorService uiEventExecutor;
    private ExecutorService javafxExecutor;
    private CountDownLatch terminationLatch;
    private LogicThread logicThread;
    private ViewThread viewThread;
    private UIEventThread uiEventThread;

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        LocalDateTime startTime = LocalDateTime.now();
        logStartup(startTime);

        initializeSystem();

        try {
            startThreads(args);
            waitForTermination();
        } catch (Exception e) {
            handleFatalError(e);
        } finally {
            cleanup(startTime);
        }
    }

    private void logStartup(LocalDateTime startTime) {
        String date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(startTime);
        logger.info("=== Démarrage de l'application Ethanpod ===");
        logger.info("Heure de démarrage: {}", date);
    }

    private void initializeSystem() {
        logicThread = new LogicThread();
        viewThread = new ViewThread();
        uiEventThread = new UIEventThread();

        DatabaseManager.getInstance().initialize();

        logicExecutor = createExecutor("LogicThread");
        viewExecutor = createExecutor("ViewThread");
        uiEventExecutor = createExecutor("UIEventThread");
        javafxExecutor = createExecutor("JavaFX-Thread");
        terminationLatch = new CountDownLatch(4); // 4 threads maintenant

        logger.info("Système multithread initialisé");
    }

    private ExecutorService createExecutor(String threadName) {
        return Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, threadName);
            t.setDaemon(false);
            return t;
        });
    }

    private void startThreads(String[] args) {
        // Chaque thread utilise son propre executor
        CompletableFuture<Void> logicFuture = startLogicThread();
        CompletableFuture<Void> uiEventFuture = startUIEventThread();
        CompletableFuture<Void> viewFuture = startViewThread();
        CompletableFuture<Void> javafxFuture = startJavaFXThread(args);

        // Monitoring des threads
        CompletableFuture.allOf(logicFuture, uiEventFuture, viewFuture, javafxFuture)
                .exceptionally(this::handleThreadException);
    }

    private CompletableFuture<Void> startLogicThread() {
        return CompletableFuture.runAsync(() -> {
            logger.info("Démarrage du thread de logique métier");
            try {
                logicThread.run();
            } catch (Exception e) {
                logger.error("Erreur dans le thread de logique métier", e);
                isRunning.set(false);
            } finally {
                logger.info("Fin du thread de logique métier");
                terminationLatch.countDown();
            }
        }, logicExecutor);
    }

    private CompletableFuture<Void> startUIEventThread() {
        return CompletableFuture.runAsync(() -> {
            logger.info("Démarrage du thread de Ui Event");
            try {
                uiEventThread.run();
            } catch (Exception e) {
                logger.error("Erreur dans le thread de Ui Event", e);
                isRunning.set(false);
            } finally {
                logger.info("Fin du thread de Ui Event");
                terminationLatch.countDown();
            }
        }, uiEventExecutor);
    }

    private CompletableFuture<Void> startViewThread() {
        return CompletableFuture.runAsync(() -> {
            logger.info("Démarrage du thread de traitement des messages (ViewThread)");
            try {
                viewThread.run();
            } catch (Exception e) {
                logger.error("Erreur dans le ViewThread", e);
                isRunning.set(false);
            } finally {
                logger.info("Fin du thread de traitement des messages");
                terminationLatch.countDown();
            }
        }, viewExecutor);
    }

    private CompletableFuture<Void> startJavaFXThread(String[] args) {
        return CompletableFuture.runAsync(() -> {
            logger.info("Démarrage du thread JavaFX");
            try {
                // Monitoring de l'état global pendant l'exécution de JavaFX
                monitorApplicationState();

                // Démarrage de JavaFX
                fr.github.ethanpod.view.Main.main(args);

            } catch (Exception e) {
                logger.error("Erreur lors du démarrage de JavaFX", e);
                isRunning.set(false);
            } finally {
                logger.info("Fin du thread JavaFX");
                cleanupApplication();
                terminationLatch.countDown();
            }
        }, javafxExecutor);
    }

    private void monitorApplicationState() {
        // Créer un thread de monitoring pour surveiller l'état de l'application
        Thread monitorThread = new Thread(() -> {
            while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000); // Vérifier chaque seconde
                    // Ici vous pouvez ajouter des vérifications d'état si nécessaire
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "ApplicationMonitor");

        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    private void cleanupApplication() {
        logger.info("Début de l'arrêt coordonné des threads");
        isRunning.set(false);

        // Arrêt gracieux des threads dans l'ordre approprié
        if (uiEventThread != null) {
            logger.info("Arrêt du UIEventThread");
            uiEventThread.stop();
        }

        if (viewThread != null) {
            logger.info("Demande d'arrêt gracieux du ViewThread");
            viewThread.requestShutdown();

            // Attendre un peu pour l'arrêt gracieux
            try {
                Thread.sleep(2000);
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
        }

        if (logicThread != null) {
            logger.info("Arrêt du LogicThread");
            logicThread.stop();
        }
    }

    private Void handleThreadException(Throwable e) {
        logger.error("Exception dans l'un des threads: {}", e.getMessage());
        isRunning.set(false);

        // Arrêt d'urgence de tous les threads
        if (logicThread != null) logicThread.stop();
        if (uiEventThread != null) uiEventThread.stop();
        if (viewThread != null) viewThread.stop();

        return null;
    }

    private void waitForTermination() throws InterruptedException {
        boolean terminated = terminationLatch.await(1, TimeUnit.HOURS);
        if (!terminated) {
            logger.warn("Les threads ne se sont pas terminés dans le délai imparti");
            forceShutdown();
        }
    }

    private void forceShutdown() {
        logger.warn("Arrêt forcé de tous les threads");
        if (logicThread != null) logicThread.stop();
        if (uiEventThread != null) uiEventThread.stop();
        if (viewThread != null) viewThread.stop();
    }

    private void handleFatalError(Exception e) {
        logger.error("Erreur fatale lors de l'initialisation de l'application", e);
        logger.error("Détails de l'erreur: {}", e.getMessage());

        if (e.getCause() != null) {
            logger.error("Cause racine: {}", e.getCause().getMessage());
        }

        logger.info("Arrêt de l'application suite à une erreur");
        System.exit(1);
    }

    private void cleanup(LocalDateTime startTime) {
        try {
            Platform.exit();
        } catch (Exception e) {
            logger.warn("Erreur lors de l'arrêt de JavaFX", e);
        }

        // Arrêt final des threads si pas encore fait
        if (logicThread != null) logicThread.stop();
        if (uiEventThread != null) uiEventThread.stop();
        if (viewThread != null) viewThread.stop();

        // Arrêt des executors
        shutdownExecutor(logicExecutor, "Logique");
        shutdownExecutor(viewExecutor, "Vue");
        shutdownExecutor(uiEventExecutor, "Event");
        shutdownExecutor(javafxExecutor, "JavaFX");

        logShutdown(startTime);
    }

    private void logShutdown(LocalDateTime startTime) {
        LocalDateTime endTime = LocalDateTime.now();
        long duration = endTime.toEpochSecond(java.time.ZoneOffset.UTC) -
                startTime.toEpochSecond(java.time.ZoneOffset.UTC);
        logger.info("Durée d'exécution: {} secondes", duration);
        logger.info("=== Fermeture de l'application Ethanpod Multithread ===");
    }

    private void shutdownExecutor(ExecutorService executor, String name) {
        try {
            logger.info("Arrêt du thread {}", name);
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("Le thread {} ne s'est pas terminé proprement, forçage de l'arrêt", name);
                executor.shutdownNow();
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            logger.warn("Interruption lors de l'arrêt du thread {}", name);
            executor.shutdownNow();
        }
    }
}
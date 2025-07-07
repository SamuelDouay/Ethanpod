package fr.github.ethanpod.app;

import fr.github.ethanpod.logic.LogicThread;
import fr.github.ethanpod.view.thread.ViewThread;
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
    private CountDownLatch terminationLatch;
    private LogicThread logicThread;
    private ViewThread viewThread;

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
        logger.info("=== Démarrage de l'application AntennaPod ===");
        logger.info("Heure de démarrage: {}", date);
    }

    private void initializeSystem() {
        // Créer les threads
        logicThread = new LogicThread();
        viewThread = new ViewThread();

        // Initialiser les executors
        logicExecutor = createExecutor("LogicThread");
        viewExecutor = createExecutor("ViewThread");
        terminationLatch = new CountDownLatch(2);

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
        CompletableFuture<Void> logicFuture = startLogicThread();
        CompletableFuture<Void> viewFuture = startViewThread(args);

        // Monitoring des threads
        CompletableFuture.allOf(logicFuture, viewFuture)
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

    private CompletableFuture<Void> startViewThread(String[] args) {
        return CompletableFuture.runAsync(() -> {
            logger.info("Démarrage du thread d'interface utilisateur");

            Thread messageProcessingThread = null;
            Thread javafxThread = null;

            try {
                messageProcessingThread = startMessageProcessingThread();
                Thread.sleep(500);
                javafxThread = startJavaFXThread(args);
                monitorThreads(javafxThread);
                waitForThreadsCompletion(messageProcessingThread, javafxThread);

            } catch (InterruptedException e) {
                logger.error("Interruption du thread d'interface utilisateur", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Erreur dans le thread d'interface utilisateur", e);
            } finally {
                cleanupViewThread();
            }
        }, viewExecutor);
    }

    private Thread startMessageProcessingThread() {
        Thread messageProcessingThread = new Thread(() -> {
            try {
                viewThread.run();
            } catch (Exception e) {
                logger.error("Erreur dans le ViewThread", e);
                isRunning.set(false);
            }
        }, "ViewThread");

        messageProcessingThread.setDaemon(false);
        messageProcessingThread.start();
        logger.info("Thread de traitement des messages démarré");

        return messageProcessingThread;
    }

    private Thread startJavaFXThread(String[] args) {
        Thread javafxThread = new Thread(() -> {
            try {
                fr.github.ethanpod.view.Main.main(args);
            } catch (Exception e) {
                logger.error("Erreur lors du démarrage de JavaFX", e);
                isRunning.set(false);
            }
        }, "JavaFX-Thread");

        javafxThread.setDaemon(false);
        javafxThread.start();
        logger.info("Thread JavaFX démarré");

        return javafxThread;
    }

    private void monitorThreads(Thread javafxThread) throws InterruptedException {
        logger.info("Surveillance des threads de l'interface utilisateur");

        while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
            Thread.sleep(1000);

            // Vérifier si JavaFX s'est terminé
            if (!javafxThread.isAlive()) {
                logger.info("JavaFX s'est terminé naturellement");
                break;
            }
        }
    }

    private void waitForThreadsCompletion(Thread messageProcessingThread, Thread javafxThread)
            throws InterruptedException {

        if (messageProcessingThread != null && messageProcessingThread.isAlive()) {
            logger.info("Attente de la fin du thread de traitement des messages...");
            messageProcessingThread.join(5000);

            if (messageProcessingThread.isAlive()) {
                logger.warn("Le thread de traitement des messages ne s'est pas terminé dans les temps");
            }
        }

        if (javafxThread != null && javafxThread.isAlive()) {
            logger.info("Attente de la fin du thread JavaFX...");
            javafxThread.join(5000);

            if (javafxThread.isAlive()) {
                logger.warn("Le thread JavaFX ne s'est pas terminé dans les temps");
            }
        }
    }

    private void cleanupViewThread() {
        logger.info("Nettoyage du thread d'interface utilisateur");

        // Marquer l'application comme arrêtée
        isRunning.set(false);

        // Arrêter le thread de logique métier
        if (logicThread != null) {
            logicThread.stop();
        }

        // Signaler la fin du thread view
        terminationLatch.countDown();

        logger.info("Fin du thread d'interface utilisateur");
    }

    private Void handleThreadException(Throwable e) {
        logger.error("Exception dans l'un des threads: {}", e.getMessage());
        isRunning.set(false);

        // Arrêter les threads proprement
        if (logicThread != null) logicThread.stop();
        if (viewThread != null) viewThread.stop();

        return null;
    }

    private void waitForTermination() throws InterruptedException {
        boolean terminated = terminationLatch.await(1, TimeUnit.HOURS);
        if (!terminated) {
            logger.warn("Les threads ne se sont pas terminés dans le délai imparti");
            // Forcer l'arrêt
            if (logicThread != null) logicThread.stop();
            if (viewThread != null) viewThread.stop();
        }
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
        // Arrêter JavaFX Platform
        try {
            Platform.exit();
        } catch (Exception e) {
            logger.warn("Erreur lors de l'arrêt de JavaFX", e);
        }

        // Arrêter les threads d'abord
        if (logicThread != null) logicThread.stop();
        if (viewThread != null) viewThread.stop();

        // Puis les executors
        shutdownExecutor(logicExecutor, "Logique");
        shutdownExecutor(viewExecutor, "Vue");

        logShutdown(startTime);
    }

    private void logShutdown(LocalDateTime startTime) {
        LocalDateTime endTime = LocalDateTime.now();
        long duration = endTime.toEpochSecond(java.time.ZoneOffset.UTC) -
                startTime.toEpochSecond(java.time.ZoneOffset.UTC);
        logger.info("Durée d'exécution: {} secondes", duration);
        logger.info("=== Fermeture de l'application AntennaPod Multithread ===");
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
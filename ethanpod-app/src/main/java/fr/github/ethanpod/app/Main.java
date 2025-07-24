package fr.github.ethanpod.app;


import fr.github.ethanpod.controller.ViewThread;
import fr.github.ethanpod.logic.LogicThread;
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
    private static final int SHUTDOWN_TIMEOUT_MS = 15000;
    private static final int CHECK_INTERVAL_MS = 1000;
    private ExecutorService logicExecutor;
    private ExecutorService viewExecutor;
    private ExecutorService uiEventExecutor;
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

        logicExecutor = createExecutor("LogicThread");
        viewExecutor = createExecutor("ViewThread");
        uiEventExecutor = createExecutor("UIEventThread");
        terminationLatch = new CountDownLatch(3);

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
        CompletableFuture<Void> uiEventFuture = startUIEventThread();
        CompletableFuture<Void> viewFuture = startViewThread(args);

        // Monitoring des threads
        CompletableFuture.allOf(logicFuture, uiEventFuture, viewFuture)
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

    private CompletableFuture<Void> startViewThread(String[] args) {
        return CompletableFuture.runAsync(() -> {
            logger.info("Démarrage du thread d'interface utilisateur");

            try {
                Thread messageProcessingThread = startMessageProcessingThread();
                Thread javafxThread = startJavaFXThread(args);
                monitorThreads(javafxThread);
                waitForThreadsCompletion(messageProcessingThread);

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

    private void monitorThreads(Thread javafxThread) {
        logger.info("Surveillance des threads de l'interface utilisateur");

        while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
            // Vérifier si JavaFX s'est terminé
            if (!javafxThread.isAlive()) {
                logger.info("JavaFX s'est terminé naturellement");
                break;
            }
        }
    }

    private void waitForThreadsCompletion(Thread messageProcessingThread)
            throws InterruptedException {

        if (messageProcessingThread != null && messageProcessingThread.isAlive()) {
            logger.info("Demande d'arrêt gracieux du thread de traitement...");
            if (viewThread != null) {
                viewThread.requestShutdown(); // ⚠️ Nouvelle méthode à ajouter
            }

            // Attendre avec vérification périodique
            long startTime = System.currentTimeMillis();
            while (messageProcessingThread.isAlive() &&
                    (System.currentTimeMillis() - startTime) < SHUTDOWN_TIMEOUT_MS) {
                Thread.sleep(CHECK_INTERVAL_MS);
            }

            if (messageProcessingThread.isAlive()) {
                logger.warn("Interruption forcée du thread de traitement");
                messageProcessingThread.interrupt();
                messageProcessingThread.join(2000);
            }
        }
    }

    private void cleanupViewThread() {
        logger.info("Début de l'arrêt coordonné des threads");
        isRunning.set(false);

        // 1. Arrêter UIEventThread en premier
        if (uiEventThread != null) {
            uiEventThread.stop();
        }

        // 2. Demander arrêt gracieux ViewThread
        if (viewThread != null) {
            viewThread.requestShutdown(); // ⚠️ Nouvelle méthode
            try {
                Thread.sleep(2000); // Laisser le temps de traiter les derniers messages
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
        }

        // 3. Arrêter LogicThread en dernier
        if (logicThread != null) {
            logicThread.stop();
        }

        terminationLatch.countDown();
        logger.info("Fin du thread d'interface utilisateur");
    }

    private Void handleThreadException(Throwable e) {
        logger.error("Exception dans l'un des threads: {}", e.getMessage());
        isRunning.set(false);
        if (logicThread != null) logicThread.stop();
        if (uiEventThread != null) uiEventThread.stop();
        if (viewThread != null) viewThread.stop();

        return null;
    }

    private void waitForTermination() throws InterruptedException {
        boolean terminated = terminationLatch.await(1, TimeUnit.HOURS);
        if (!terminated) {
            logger.warn("Les threads ne se sont pas terminés dans le délai imparti");
            if (logicThread != null) logicThread.stop();
            if (uiEventThread != null) uiEventThread.stop();
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
        try {
            Platform.exit();
        } catch (Exception e) {
            logger.warn("Erreur lors de l'arrêt de JavaFX", e);
        }

        if (logicThread != null) logicThread.stop();
        if (uiEventThread != null) uiEventThread.stop();
        if (viewThread != null) viewThread.stop();

        shutdownExecutor(logicExecutor, "Logique");
        shutdownExecutor(viewExecutor, "Vue");
        shutdownExecutor(uiEventExecutor, "Event");

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
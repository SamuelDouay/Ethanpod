package fr.github.ethanpod.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final ExecutorService logicExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "LogicThread");
        t.setDaemon(false); // Thread non-daemon pour qu'il ne s'arrête pas automatiquement
        return t;
    });
    private static final ExecutorService viewExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "ViewThread");
        t.setDaemon(false);
        return t;
    });
    private static final AtomicBoolean isRunning = new AtomicBoolean(true);
    private static final CountDownLatch terminationLatch = new CountDownLatch(2); // Un pour chaque thread

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.now();

        logger.info("=== Démarrage de l'application AntennaPod ===");
        logger.info("Heure de démarrage: {}",
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(startTime));

        if (args.length > 0) {
            logger.info("Arguments de démarrage reçus: {}", String.join(", ", args));
        }

        try {
            // Démarrer la logique métier dans son propre thread
            CompletableFuture<Void> logicFuture = CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("LogicThread");
                logger.info("Démarrage de la logique métier dans un thread dédié");
                try {
                    fr.github.ethanpod.logic.Main.start();
                    // Maintenir la logique en vie jusqu'à ce que l'application soit terminée
                    while (isRunning.get()) {
                        try {
                            Thread.sleep(1000); // Vérification périodique si l'application doit s'arrêter
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.info("Thread de logique interrompu");
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.error("Erreur dans le thread de logique métier", e);
                } finally {
                    logger.info("Fin du thread de logique métier");
                    terminationLatch.countDown();
                }
            }, logicExecutor);

            // Démarrer l'interface utilisateur dans son propre thread
            CompletableFuture<Void> viewFuture = CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("ViewThread");
                logger.info("Démarrage de l'interface utilisateur dans un thread dédié");
                try {
                    fr.github.ethanpod.view.Main.main(args);
                } catch (Exception e) {
                    logger.error("Erreur dans le thread d'interface utilisateur", e);
                } finally {
                    logger.info("Fin du thread d'interface utilisateur");
                    terminationLatch.countDown();
                    // Si l'UI se termine, signaler à la logique de s'arrêter aussi
                    isRunning.set(false);
                }
            }, viewExecutor);

            // Gérer les exceptions des deux threads
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(logicFuture, viewFuture);
            combinedFuture.exceptionally(e -> {
                logger.error("Exception dans l'un des threads: {}", e.getMessage());
                isRunning.set(false);
                return null;
            });

            // Attendre que les deux threads se terminent (avec timeout)
            boolean allThreadsTerminated = terminationLatch.await(1, TimeUnit.HOURS);
            if (!allThreadsTerminated) {
                logger.warn("Les threads ne se sont pas terminés dans le délai imparti");
            }

        } catch (Exception e) {
            logger.error("Erreur fatale lors de l'initialisation de l'application", e);
            logger.error("Détails de l'erreur: {}", e.getMessage());

            if (e.getCause() != null) {
                logger.error("Cause racine: {}", e.getCause().getMessage());
            }

            logger.info("Arrêt de l'application suite à une erreur");
            System.exit(1);
        } finally {
            // Arrêt propre des services d'exécution
            shutdownExecutor(logicExecutor, "Logique");
            shutdownExecutor(viewExecutor, "Vue");

            LocalDateTime endTime = LocalDateTime.now();
            logger.info("Durée d'exécution: {} secondes",
                    (endTime.toEpochSecond(java.time.ZoneOffset.UTC) -
                            startTime.toEpochSecond(java.time.ZoneOffset.UTC)));
            logger.info("=== Fermeture de l'application AntennaPod ===");
        }
    }

    // Méthode utilitaire pour arrêter proprement un ExecutorService
    private static void shutdownExecutor(ExecutorService executor, String name) {
        try {
            logger.info("Arrêt du thread {}", name);
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("Le thread {} ne s'est pas terminé proprement, forçage de l'arrêt", name);
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.warn("Interruption lors de l'arrêt du thread {}", name);
            executor.shutdownNow();
        }
    }

    // Méthode publique pour arrêter proprement l'application
    public static void shutdown() {
        logger.info("Demande d'arrêt de l'application reçue");
        isRunning.set(false);

        // Arrêt des ExecutorService
        shutdownExecutor(logicExecutor, "Logique");
        shutdownExecutor(viewExecutor, "Vue");

        logger.info("Tous les threads ont été arrêtés");
    }
}
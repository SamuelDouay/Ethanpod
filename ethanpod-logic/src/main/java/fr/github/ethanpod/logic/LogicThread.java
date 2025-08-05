package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LogicThread implements Runnable {
    private final Logger logger = LogManager.getLogger(LogicThread.class);
    private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
    private final LogicHandle logicHandler;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ExecutorService asyncExecutor = Executors.newCachedThreadPool(
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Logic-" + threadNumber.getAndIncrement());
                    thread.setDaemon(true); // Changé en daemon
                    return thread;
                }
            }
    );
    private Thread currentThread;

    public LogicThread() {
        this.logicHandler = new LogicHandle(MessageRouter.getInstance().registerThread("LogicThread"), asyncExecutor);
    }

    public void requestShutdown() {
        logger.debug("Demande d'arrêt gracieux du LogicThread");
        shutdownRequested.set(true);

        // Envoyer un signal de shutdown au handler
        logicHandler.sendShutdownSignal();

        // En backup: interrompre le thread si nécessaire
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
        }
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();
        logger.info("Thread Logique démarré - Traitement des données");

        try {
            startPeriodicTasks();

            while (!shutdownRequested.get() && !Thread.currentThread().isInterrupted()) {

                logicHandler.processIncomingMessages();

            }
        } catch (Exception e) {
            logger.error("Erreur critique dans le thread de logique", e);
        } finally {
            cleanup();
        }

        logger.info("Thread Logique terminé");
    }

    private void startPeriodicTasks() {
        scheduler.scheduleAtFixedRate(() -> {
            if (!shutdownRequested.get()) {
                try {
                    logicHandler.refreshData();
                } catch (Exception e) {
                    logger.warn("Erreur lors du refresh périodique", e);
                }
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    private void cleanup() {
        logger.debug("Nettoyage du LogicThread...");

        // Arrêt des tâches périodiques
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Arrêt de l'executor asynchrone
        asyncExecutor.shutdown();
        try {
            if (!asyncExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            asyncExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Traitement des derniers messages
        if (shutdownRequested.get()) {
            try {
                logicHandler.flushPendingMessages();
            } catch (Exception e) {
                logger.warn("Erreur lors du flush des messages", e);
            }
        }

        // Arrêt de la base de données
        try {
            DatabaseManager.getInstance().shutdown();
        } catch (Exception e) {
            logger.warn("Erreur lors de l'arrêt de la base de données", e);
        }
    }

    public void stop() {
        logger.debug("Arrêt du thread de logique demandé");
        requestShutdown(); // Utilise la méthode principale
    }
}
package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.ThreadMessage;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LogicThread implements Runnable {
    private final Logger logger = LogManager.getLogger(LogicThread.class);
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final LogicHandle logicHandler;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final ExecutorService asyncExecutor;
    private final DatabaseManager databaseManager;


    public LogicThread(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.asyncExecutor = createVirtualThreadExecutor();
        BlockingQueue<ThreadMessage> messageQueue = MessageRouter.getInstance().registerThread("LogicThread");
        this.logicHandler = new LogicHandle(messageQueue, asyncExecutor, databaseManager);
    }

    private ExecutorService createVirtualThreadExecutor() {
        AtomicInteger threadCounter = new AtomicInteger(1);

        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual()
                        .name("Logic-", threadCounter.getAndIncrement())
                        .factory()
        );
    }

    @Override
    public void run() {
        logger.info("Thread Logique démarré - Traitement des données");

        try {
            startPeriodicTasks();

            while (running.get()) {
                try {
                    logicHandler.processIncomingMessages();

                } catch (InterruptedException _) {
                    logger.debug("Thread Logique interrompu");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("Erreur dans le thread de logique", e);
                }
            }
        } finally {
            scheduler.shutdown();
            asyncExecutor.shutdown();
            logger.info("Thread Logique terminé");
        }
    }

    private void startPeriodicTasks() {
        scheduler.scheduleAtFixedRate(() -> {
            if (running.get()) {
                logicHandler.refreshData();
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    public void stop() {
        logger.debug("Arrêt du thread de logique demandé");
        running.set(false);
        scheduler.shutdown();
        asyncExecutor.shutdown();
        this.databaseManager.shutdown();
    }

}
package fr.github.ethanpod.logic;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.ThreadMessage;
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
    private final ExecutorService asyncExecutor = Executors.newCachedThreadPool(
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("LogicThread-" + threadNumber.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                }
            }
    );

    public LogicThread() {
        MessageRouter messageRouter = MessageRouter.getInstance();
        BlockingQueue<ThreadMessage> messageQueue = messageRouter.registerThread("LogicThread");
        this.logicHandler = new LogicHandle(messageQueue, asyncExecutor);
    }

    @Override
    public void run() {
        logger.info("🔵 Thread Logique démarré - Traitement des données");

        try {
            startPeriodicTasks();

            while (running.get()) {
                try {
                    logicHandler.processIncomingMessages();

                } catch (InterruptedException _) {
                    logger.info("🔵 Thread Logique interrompu");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("🔵 Erreur dans le thread de logique", e);
                }
            }
        } finally {
            scheduler.shutdown();
            asyncExecutor.shutdown();
            logger.info("🔵 Thread Logique terminé");
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
        logger.info("🔵 Arrêt du thread de logique demandé");
        running.set(false);
        scheduler.shutdown();
        asyncExecutor.shutdown();
    }

}
package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.NotificationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(ViewThread.class);
    private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
    private final MessageRouter messageRouter = MessageRouter.getInstance();
    private final ViewHandle viewHandle;
    private Thread currentThread;

    public ViewThread() {
        this.viewHandle = new ViewHandle(MessageRouter.getInstance().registerThread("ViewThread"));
    }

    public void requestShutdown() {
        logger.debug("Demande d'arrêt gracieux du ViewThread");
        shutdownRequested.set(true);

        // Envoyer un message de shutdown pour réveiller le thread
        viewHandle.sendShutdownSignal();

        // En backup: interrompre le thread si nécessaire
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
        }
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();
        logger.info("Thread View démarré - Interface utilisateur");

        messageRouter.sendNotification(MessageRouter.VIEW_THREAD, MessageRouter.LOGIC_THREAD, NotificationType.UI_READY);

        try {
            while (!shutdownRequested.get() && !Thread.currentThread().isInterrupted()) {

                viewHandle.processIncomingMessages();

            }
        } catch (Exception e) {
            logger.error("Erreur critique dans le thread d'interface", e);
        } finally {
            cleanup();
        }

        logger.info("Thread View terminé");
    }

    private void cleanup() {
        if (shutdownRequested.get()) {
            logger.info("Traitement des derniers messages...");
            try {
                viewHandle.flushPendingMessages();
            } catch (Exception e) {
                logger.warn("Erreur lors du flush des messages", e);
            }
        }

        try {
            viewHandle.stopAllService();
        } catch (Exception e) {
            logger.warn("Erreur lors de l'arrêt des services", e);
        }
    }

    public void stop() {
        logger.debug("Arrêt du thread d'interface demandé");
        requestShutdown(); // Utilise la méthode principale
    }
}
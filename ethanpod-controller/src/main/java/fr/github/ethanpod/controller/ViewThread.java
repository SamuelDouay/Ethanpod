package fr.github.ethanpod.controller;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(ViewThread.class);
    private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
    private final MessageRouter messageRouter;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ViewHandle viewHandle;

    public ViewThread() {
        this.messageRouter = MessageRouter.getInstance();
        this.viewHandle = new ViewHandle();
    }

    public void requestShutdown() {
        logger.debug("🟢 Demande d'arrêt gracieux du ViewThread");
        shutdownRequested.set(true);
        viewHandle.interruptProcessing(); // ⚠️ Nouvelle méthode ViewHandle
    }

    @Override
    public void run() {
        logger.info("🟢 Thread View démarré - Interface utilisateur");

        messageRouter.sendRequestToLogicFromView("UI_READY", null, MessageType.NOTIFICATION, null);
        boolean shouldExit = false;

        while (running.get() && !shutdownRequested.get() && !shouldExit) {
            try {
                viewHandle.processIncomingMessages();

                if (Thread.currentThread().isInterrupted()) {
                    logger.info("🟢 Thread View interrompu volontairement");
                    shouldExit = true;
                }

            } catch (InterruptedException _) {
                logger.info("🟢 Thread View interrompu");
                Thread.currentThread().interrupt();
                shouldExit = true;
            } catch (Exception e) {
                logger.error("Erreur dans le thread d'interface", e);
                if (shutdownRequested.get()) {
                    shouldExit = true;
                }
            }
        }

        if (shutdownRequested.get()) {
            logger.info("🟢 Traitement des derniers messages...");
            viewHandle.flushPendingMessages();
        }

        logger.info("🟢 Thread View terminé");
    }

    public void stop() {
        logger.debug("🟢 Arrêt du thread d'interface demandé");
        shutdownRequested.set(true);  // Ajouter cette ligne
        running.set(false);
        viewHandle.stopAllService();
    }
}
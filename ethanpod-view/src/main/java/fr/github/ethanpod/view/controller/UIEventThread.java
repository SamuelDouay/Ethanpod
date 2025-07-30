package fr.github.ethanpod.view.controller;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class UIEventThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(UIEventThread.class);
    private final MessageRouter messageRouter;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final UIEventHandle uiEventHandle;

    public UIEventThread() {
        this.messageRouter = MessageRouter.getInstance();
        this.uiEventHandle = new UIEventHandle();
    }

    @Override
    public void run() {
        logger.info("Thread Event démarré - Event UI");

        messageRouter.sendRequestToViewFromEvent("UI_EVENT_READY", null, MessageType.NOTIFICATION, null);
        while (running.get()) {
            try {
                uiEventHandle.processIncomingMessages();

                if (Thread.currentThread().isInterrupted()) {
                    logger.debug("Thread Event interrompu volontairement");
                    break;
                }
            } catch (InterruptedException _) {
                logger.debug("Thread Event interrompu");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Erreur dans le thread d'interface", e);
            }
        }

        logger.info("Thread Event terminé");
    }

    public void stop() {
        logger.debug("Arrêt du thread d'interface demandé");
        running.set(false);
    }
}
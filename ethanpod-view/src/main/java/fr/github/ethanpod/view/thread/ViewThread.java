package fr.github.ethanpod.view.thread;

import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.view.layout.NavigationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(ViewThread.class);
    private static ViewThread instance;
    private final MessageRouter messageRouter;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ViewHandle viewHandle;

    public ViewThread() {
        this.messageRouter = MessageRouter.getInstance();
        this.viewHandle = new ViewHandle();
        instance = this;
    }

    public static ViewThread getInstance() {
        return instance;
    }

    @Override
    public void run() {
        logger.info("🟢 Thread View démarré - Interface utilisateur");

        messageRouter.sendRequestToLogic("UI_READY", null, MessageType.NOTIFICATION, null);
        while (running.get()) {
            try {
                viewHandle.processIncomingMessages();

                if (Thread.currentThread().isInterrupted()) {
                    logger.info("🟢 Thread View interrompu volontairement");
                    break;
                }
            } catch (InterruptedException _) {
                logger.info("🟢 Thread View interrompu");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Erreur dans le thread d'interface", e);
            }
        }

        logger.info("🟢 Thread View terminé");
    }

    public void stop() {
        logger.info("🟢 Arrêt du thread d'interface demandé");
        running.set(false);
        viewHandle.stopAllService();
    }

    public void setNavigationContainer(NavigationContainer navigationContainer) {
        this.viewHandle.getControllerManager().setNavigationContainer(navigationContainer);
        logger.info("🟢 NavigationContainer configuré dans ViewThread");
    }
}
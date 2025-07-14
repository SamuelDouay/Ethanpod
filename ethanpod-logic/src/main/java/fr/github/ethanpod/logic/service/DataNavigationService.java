package fr.github.ethanpod.logic.service;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.core.thread.MessageRouter;
import fr.github.ethanpod.core.thread.MessageType;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class DataNavigationService extends DataService {
    private static final Logger logger = LogManager.getLogger(DataNavigationService.class);
    private final NavigationDao navigationDao;
    private final MessageRouter messageRouter = MessageRouter.getInstance();

    public DataNavigationService(ExecutorService executor) {
        super(executor);
        this.navigationDao = new NavigationDao();
    }

    @Override
    void refreshData() {
        
    }

    public void getNavigationListAsync(String requestId) {

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        List<NavigationItem> navigationList = navigationDao.getList();
                        logger.info("🔵 {} éléments récupérés", navigationList.size());
                        return navigationList;
                    } catch (Exception e) {
                        throw new RuntimeException("Error getting navigation list", e);
                    }
                }, executor)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Erreur navigation list", throwable);
                        messageRouter.sendRequestToView("ERROR", requestId, MessageType.ERROR, throwable.getMessage());
                    } else {
                        messageRouter.sendRequestToView("NAVIGATION_LIST_RESULT", requestId, MessageType.RESPONSE, result);
                    }
                });
    }
}

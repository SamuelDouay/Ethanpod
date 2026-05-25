package fr.github.ethanpod.logic.handler;

import fr.github.ethanpod.logic.sql.dao.*;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;

import java.util.List;

public final class HandlerInitializer {
    public static void initializeAll(DatabaseManager dbManager) {
        List<Runnable> initializers = List.of(
                () -> new NavigationRequestHandler(new NavigationDao(dbManager)),
                () -> new InboxRequestHandler(new InboxDao(dbManager)),
                () -> new DownloadRequestHandler(new DownloadDao(dbManager)),
                () -> new EpisodeRequestHandler(new EpisodeDao(dbManager)),
                () -> new QueueRequestHandler(new QueueDao(dbManager)),
                () -> new PodcastRequestHandler(new PodcastDao(dbManager)),
                () -> new SurpriseRequestHandler(new SurpriseDao(dbManager)),
                () -> new HistoryRequestHandler(new HistoryDao(dbManager))
        );
        initializers.forEach(Runnable::run);
    }
}

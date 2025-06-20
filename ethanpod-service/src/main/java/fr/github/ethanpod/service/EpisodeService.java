package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.logic.sql.dao.EpisodeDao;

import java.util.List;

public class EpisodeService {
    private final EpisodeDao episodeDao;

    public EpisodeService() {
        this.episodeDao = new EpisodeDao();
    }

    public List<EpisodeItem> getTop8Queue() {
        return episodeDao.getTop8InQueue();
    }

    public List<EpisodeItem> getNewsTop8() {
        return episodeDao.getNewsTop8();
    }
}

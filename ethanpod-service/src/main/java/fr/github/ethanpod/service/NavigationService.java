package fr.github.ethanpod.service;

import fr.github.ethanpod.core.item.NavigationItem;
import fr.github.ethanpod.logic.sql.dao.NavigationDao;

import java.util.List;

public class NavigationService {
    private final NavigationDao navigationDao;

    public NavigationService() {
        this.navigationDao = new NavigationDao();
    }

    public List<NavigationItem> getList() {
        return navigationDao.getList();
    }

    public int getNumberOfInbox() {
        return navigationDao.getNumberOfInbox();
    }
}

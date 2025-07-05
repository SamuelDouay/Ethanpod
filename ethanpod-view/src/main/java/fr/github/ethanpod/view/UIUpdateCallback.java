package fr.github.ethanpod.view;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public interface UIUpdateCallback {
    void updateNavigationList(List<NavigationItem> navigationList);

    void updateInboxCount(Integer count);

    void showError(String errorMessage);

    void showNotification(String notification);
}

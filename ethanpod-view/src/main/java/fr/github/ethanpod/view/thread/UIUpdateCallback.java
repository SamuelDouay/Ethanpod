package fr.github.ethanpod.view.thread;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public interface UIUpdateCallback {

    void updateNavigationList(List<NavigationItem> navigationList);

    void updateInboxCount(Integer count);

    void showNotification(String message, NotificationType type);

    void showError(String errorMessage);

    void updateLoadingState(boolean isLoading);

    enum NotificationType {
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }
}

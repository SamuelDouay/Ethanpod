package fr.github.ethanpod.view.thread.callback;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public interface UIUpdateCallback {

    void updateNavigationList(List<NavigationItem> navigationList);

    void updateInboxCount(Integer count);

    void showNotification(String message);

    void showError(String errorMessage);

    void updateLoadingState(boolean isLoading);
}

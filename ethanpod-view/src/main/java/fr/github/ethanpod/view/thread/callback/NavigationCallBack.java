package fr.github.ethanpod.view.thread.callback;

import fr.github.ethanpod.core.item.NavigationItem;

import java.util.List;

public class NavigationCallBack extends CallBack {

    public void updateNavigationList(List<NavigationItem> navigationList) {
        System.out.println(navigationList);
    }
}

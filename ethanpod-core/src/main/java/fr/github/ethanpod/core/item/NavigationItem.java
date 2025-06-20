package fr.github.ethanpod.core.item;

import org.kordamp.ikonli.javafx.FontIcon;

public class NavigationItem extends Item {
    private FontIcon icon;
    private String title;
    private int number;
    private String imageUrl;

    public NavigationItem(FontIcon icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
        this.number = 0;
        this.imageUrl = null;
    }

    public NavigationItem(FontIcon icon, String title, int number) {
        super();
        this.icon = icon;
        this.title = title;
        this.number = number;
        this.imageUrl = null;
    }

    public NavigationItem(String imageUrl, String title) {
        super();
        this.imageUrl = imageUrl;
        this.title = title;
        this.number = 0;
        this.icon = null;
    }

    public NavigationItem(String imageUrl, String title, int number) {
        super();
        this.imageUrl = imageUrl;
        this.title = title;
        this.number = number;
        this.icon = null;
    }

    public FontIcon getIcon() {
        return icon;
    }

    public void setIcon(FontIcon icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

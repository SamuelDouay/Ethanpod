package fr.github.ethanpod.core.item;

public class NavigationItem extends Item {
    private final boolean icon;
    private final String title;
    private final String urlImage;
    private final Integer id;
    private int number;

    public NavigationItem(String urlImage, String title, boolean icon, Integer id) {
        super();
        this.urlImage = urlImage;
        this.title = title;
        this.number = 0;
        this.icon = icon;
        this.id = id;
    }

    public NavigationItem(String urlImage, String title, int number, boolean icon, Integer id) {
        super();
        this.urlImage = urlImage;
        this.title = title;
        this.number = number;
        this.icon = icon;
        this.id = id;
    }

    public NavigationItem(String urlImage, String title, boolean icon) {
        super();
        this.urlImage = urlImage;
        this.title = title;
        this.number = 0;
        this.icon = icon;
        this.id = 0;
    }

    public NavigationItem(String urlImage, String title, int number, boolean icon) {
        super();
        this.urlImage = urlImage;
        this.title = title;
        this.number = number;
        this.icon = icon;
        this.id = 0;
    }

    public boolean isIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public Integer getId() {
        return id;
    }
}

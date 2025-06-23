package fr.github.ethanpod.core.item;

public class NavigationItem extends Item {
    private boolean icon;
    private String title;
    private int number;
    private String name;

    public NavigationItem(String name, String title, boolean icon) {
        super();
        this.name = name;
        this.title = title;
        this.number = 0;
        this.icon = icon;
    }

    public NavigationItem(String name, String title, int number, boolean icon) {
        super();
        this.name = name;
        this.title = title;
        this.number = number;
        this.icon = icon;
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

    public String getName() {
        return name;
    }
}

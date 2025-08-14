package fr.github.ethanpod.core.item;

public class NavigationItem extends Item {
    private final boolean icon;
    private final String title;
    private final String name;
    private int number;

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

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }
}

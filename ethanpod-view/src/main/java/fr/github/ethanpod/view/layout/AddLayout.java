package fr.github.ethanpod.view.layout;

import javafx.scene.layout.VBox;

public class AddLayout extends Layout{

    public AddLayout(){
        super("Add podcast");
    }

    @Override
    public VBox getLayout() {
        VBox box = getContainer();

        box.getChildren().add(getTitle());

        return box;
    }
}

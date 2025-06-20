package fr.github.ethanpod.view.component.button;

import fr.github.ethanpod.view.util.TypeButton;
import javafx.scene.control.Button;
import org.kordamp.ikonli.javafx.FontIcon;

public class ButtonComponent {
    public ButtonComponent() {
        // no parameter
    }

    public Button createPrimaryButton(String text) {
        return new ButtonBuilder()
                .withText(text)
                .withType(TypeButton.PRIMARY)
                .build();
    }

    public Button createPrimaryButton(String text, FontIcon icon) {
        return new ButtonBuilder()
                .withText(text)
                .withIcon(icon)
                .withType(TypeButton.PRIMARY)
                .build();
    }

    public Button createPrimaryButton(FontIcon icon) {
        return new ButtonBuilder()
                .withIcon(icon)
                .withType(TypeButton.PRIMARY)
                .setIconOnly(true)
                .build();
    }

    public Button createSecondaryButton(String text) {
        return new ButtonBuilder()
                .withText(text)
                .withType(TypeButton.SECONDARY)
                .build();
    }

    public Button createSecondaryButton(String text, FontIcon icon) {
        return new ButtonBuilder()
                .withText(text)
                .withIcon(icon)
                .withType(TypeButton.SECONDARY)
                .build();
    }

    public Button createSecondaryButton(FontIcon icon) {
        return new ButtonBuilder()
                .withIcon(icon)
                .withType(TypeButton.SECONDARY)
                .setIconOnly(true)
                .build();
    }

    public Button createTertiaryButton(String text) {
        return new ButtonBuilder()
                .withText(text)
                .withType(TypeButton.TERTIARY)
                .build();
    }

    public Button createTertiaryButton(String text, FontIcon icon) {
        return new ButtonBuilder()
                .withText(text)
                .withIcon(icon)
                .withType(TypeButton.TERTIARY)
                .build();
    }

    public Button createTertiaryButton(FontIcon icon) {
        return new ButtonBuilder()
                .withIcon(icon)
                .withType(TypeButton.TERTIARY)
                .setIconOnly(true)
                .build();
    }
}

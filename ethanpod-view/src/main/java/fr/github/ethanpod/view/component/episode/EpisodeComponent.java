package fr.github.ethanpod.view.component.episode;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.view.component.badge.BadgeComponent;
import fr.github.ethanpod.view.component.button.ButtonComponent;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;

public class EpisodeComponent {
    public EpisodeComponent() {
        // no parameters
    }

    public HBox createInboxEpisode(EpisodeItem item) {
        return new EpisodeBuilder().withEpisodeItem(item)
                .withButton(new ButtonComponent().createPrimaryButton("Télécharger"))
                .withBadge(new BadgeComponent().createBlueBadge(new FontIcon(MaterialDesignI.INBOX)))
                .build();
    }
}

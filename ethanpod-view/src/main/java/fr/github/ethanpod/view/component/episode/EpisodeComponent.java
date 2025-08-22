package fr.github.ethanpod.view.component.episode;

import fr.github.ethanpod.core.item.EpisodeItem;
import fr.github.ethanpod.view.component.badge.BadgeComponent;
import fr.github.ethanpod.view.component.button.ButtonComponent;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;

public class EpisodeComponent {
    public EpisodeComponent() {
        // no parameters
    }

    public HBox createInboxEpisode(EpisodeItem item) {
        HBox badgeComponent = null;
        if (item.isInInbox())
            badgeComponent = new BadgeComponent().createBlueBadge(new FontIcon(MaterialDesignI.INBOX));
        if (item.isInQueue())
            badgeComponent = new BadgeComponent().createPurpleBadge(new FontIcon(MaterialDesignP.PLAYLIST_PLAY));

        return new EpisodeBuilder().withEpisodeItem(item)
                .withButton(new ButtonComponent().createPrimaryButton("Download"))
                .withBadge(badgeComponent)
                .isRead(item.isRead())
                .isFavoris(item.isFavorite())
                .build();

    }
}

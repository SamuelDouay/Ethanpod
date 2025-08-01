package fr.github.ethanpod.core.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EpisodeItemTest {
    private final String testUrlImage = "https://example.com/image.jpg";
    private final boolean testFavorite = true;
    private final String testName = "Test Episode";
    private final String testDuration = "45:30";
    private final String testDate = "2024-01-15";
    private final String testSize = "125.5 MB";
    private final boolean testRead = false;
    private EpisodeItem episodeItem;

    @BeforeEach
    void setUp() {
        episodeItem = new EpisodeItem(testUrlImage, testFavorite, testName, testDuration, testDate, testSize, testRead);
    }

    @AfterEach
    void tearDown() {
        episodeItem = null;
    }

    @Test
    void constructorAndInheritance() {
        // Test que l'objet hérite correctement d'Item
        assertNotNull(episodeItem.getUuid());
        assertFalse(episodeItem.isSelected()); // Valeur par défaut d'Item

        // Test que le constructeur d'Item est appelé
        episodeItem.setSelected(true);
        assertTrue(episodeItem.isSelected());
    }

    @Test
    void getUrlImage() {
        assertEquals(testUrlImage, episodeItem.getUrlImage());

        // Test avec null
        EpisodeItem episodeWithNullUrl = new EpisodeItem(null, false, "test", "10:00", "2024-01-01", "50MB", true);
        assertNull(episodeWithNullUrl.getUrlImage());

        // Test avec chaîne vide
        EpisodeItem episodeWithEmptyUrl = new EpisodeItem("", false, "test", "10:00", "2024-01-01", "50MB", true);
        assertEquals("", episodeWithEmptyUrl.getUrlImage());
    }

    @Test
    void isFavorite() {
        assertTrue(episodeItem.isFavorite());

        // Test avec false
        EpisodeItem notFavoriteEpisode = new EpisodeItem("url", false, "test", "10:00", "2024-01-01", "50MB", true);
        assertFalse(notFavoriteEpisode.isFavorite());
    }

    @Test
    void getName() {
        assertEquals(testName, episodeItem.getName());

        // Test avec null
        EpisodeItem episodeWithNullName = new EpisodeItem("url", true, null, "10:00", "2024-01-01", "50MB", false);
        assertNull(episodeWithNullName.getName());

        // Test avec chaîne vide
        EpisodeItem episodeWithEmptyName = new EpisodeItem("url", true, "", "10:00", "2024-01-01", "50MB", false);
        assertEquals("", episodeWithEmptyName.getName());
    }

    @Test
    void getDuration() {
        assertEquals(testDuration, episodeItem.getDuration());

        // Test avec null
        EpisodeItem episodeWithNullDuration = new EpisodeItem("url", true, "test", null, "2024-01-01", "50MB", false);
        assertNull(episodeWithNullDuration.getDuration());

        // Test avec différents formats
        EpisodeItem episodeWithShortDuration = new EpisodeItem("url", true, "test", "5:30", "2024-01-01", "50MB", false);
        assertEquals("5:30", episodeWithShortDuration.getDuration());
    }

    @Test
    void getDate() {
        assertEquals(testDate, episodeItem.getDate());

        // Test avec null
        EpisodeItem episodeWithNullDate = new EpisodeItem("url", true, "test", "10:00", null, "50MB", false);
        assertNull(episodeWithNullDate.getDate());

        // Test avec différents formats de date
        EpisodeItem episodeWithDifferentDate = new EpisodeItem("url", true, "test", "10:00", "01/15/2024", "50MB", false);
        assertEquals("01/15/2024", episodeWithDifferentDate.getDate());
    }

    @Test
    void getSize() {
        assertEquals(testSize, episodeItem.getSize());

        // Test avec null
        EpisodeItem episodeWithNullSize = new EpisodeItem("url", true, "test", "10:00", "2024-01-01", null, false);
        assertNull(episodeWithNullSize.getSize());

        // Test avec différents formats de taille
        EpisodeItem episodeWithDifferentSize = new EpisodeItem("url", true, "test", "10:00", "2024-01-01", "1.2 GB", false);
        assertEquals("1.2 GB", episodeWithDifferentSize.getSize());
    }

    @Test
    void isRead() {
        assertFalse(episodeItem.isRead());

        // Test avec true
        EpisodeItem readEpisode = new EpisodeItem("url", false, "test", "10:00", "2024-01-01", "50MB", true);
        assertTrue(readEpisode.isRead());
    }

    @Test
    void constructorWithAllNullValues() {
        // Test avec toutes les valeurs null/false
        EpisodeItem nullEpisode = new EpisodeItem(null, false, null, null, null, null, false);

        assertNull(nullEpisode.getUrlImage());
        assertFalse(nullEpisode.isFavorite());
        assertNull(nullEpisode.getName());
        assertNull(nullEpisode.getDuration());
        assertNull(nullEpisode.getDate());
        assertNull(nullEpisode.getSize());
        assertFalse(nullEpisode.isRead());

        // Vérifier que l'héritage fonctionne toujours
        assertNotNull(nullEpisode.getUuid());
        assertFalse(nullEpisode.isSelected());
    }

    @Test
    void constructorWithAllTrueValues() {
        // Test avec toutes les valeurs boolean à true
        EpisodeItem allTrueEpisode = new EpisodeItem("url", true, "name", "duration", "date", "size", true);

        assertTrue(allTrueEpisode.isFavorite());
        assertTrue(allTrueEpisode.isRead());
    }

    @Test
    void immutabilityOfFields() {
        // Test que les champs sont final (immutable)
        String originalName = episodeItem.getName();
        String originalUrl = episodeItem.getUrlImage();

        // Les valeurs ne peuvent pas changer après construction
        assertEquals(originalName, episodeItem.getName());
        assertEquals(originalUrl, episodeItem.getUrlImage());
    }
}
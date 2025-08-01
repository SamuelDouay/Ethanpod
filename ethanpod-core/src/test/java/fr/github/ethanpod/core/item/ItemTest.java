package fr.github.ethanpod.core.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
    }

    @AfterEach
    void tearDown() {
        item = null;
    }

    @Test
    void getUuid() {
        assertNotNull(item.getUuid());

        // Test que l'UUID est bien unique pour chaque nouvelle instance
        Item item2 = new Item();
        assertNotEquals(item.getUuid(), item2.getUuid());

        // Test que l'UUID reste constant pour la même instance
        UUID firstCall = item.getUuid();
        UUID secondCall = item.getUuid();
        assertEquals(firstCall, secondCall);
    }

    @Test
    void isSelected() {
        // Test que l'item n'est pas sélectionné par défaut
        assertFalse(item.isSelected());

        // Test après avoir changé l'état
        item.setSelected(true);
        assertTrue(item.isSelected());

        item.setSelected(false);
        assertFalse(item.isSelected());
    }

    @Test
    void setSelected() {
        // Test de changement d'état vers true
        item.setSelected(true);
        assertTrue(item.isSelected());

        // Test de changement d'état vers false
        item.setSelected(false);
        assertFalse(item.isSelected());

        // Test de changement multiple
        item.setSelected(true);
        item.setSelected(true); // Déjà true
        assertTrue(item.isSelected());

        item.setSelected(false);
        item.setSelected(false); // Déjà false
        assertFalse(item.isSelected());
    }

    @Test
    void constructorInitialization() {
        // Test que le constructeur initialise correctement les valeurs
        Item newItem = new Item();
        assertNotNull(newItem.getUuid());
        assertFalse(newItem.isSelected());
    }
}
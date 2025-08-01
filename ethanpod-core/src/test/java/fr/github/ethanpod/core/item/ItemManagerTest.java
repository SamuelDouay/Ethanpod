package fr.github.ethanpod.core.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemManagerTest {
    private ItemManager itemManager;
    private Item item1;
    private Item item2;
    private Item item3;

    @BeforeEach
    void setUp() {
        itemManager = new ItemManager();
        item1 = new Item();
        item2 = new Item();
        item3 = new Item();
    }

    @AfterEach
    void tearDown() {
        itemManager = null;
        item1 = null;
        item2 = null;
        item3 = null;
    }

    @Test
    void addItem() {
        // Test d'ajout d'un seul item
        itemManager.addItem(item1);

        // Vérification indirecte via setItemState
        itemManager.setItemState(true, item1.getUuid());
        assertTrue(item1.isSelected());

        // Test d'ajout de plusieurs items
        itemManager.addItem(item2);
        itemManager.addItem(item3);

        // Vérification que tous les items sont gérés
        itemManager.setItemState(true, item2.getUuid());
        assertTrue(item2.isSelected());
        assertFalse(item1.isSelected()); // item1 devrait être désélectionné
        assertFalse(item3.isSelected()); // item3 devrait rester non sélectionné
    }

    @Test
    void setItemStateWithValidUuid() {
        // Ajouter des items
        itemManager.addItem(item1);
        itemManager.addItem(item2);
        itemManager.addItem(item3);

        // Test sélection du premier item
        itemManager.setItemState(true, item1.getUuid());
        assertTrue(item1.isSelected());
        assertFalse(item2.isSelected());
        assertFalse(item3.isSelected());

        // Test sélection du deuxième item
        itemManager.setItemState(true, item2.getUuid());
        assertFalse(item1.isSelected()); // Devrait être désélectionné
        assertTrue(item2.isSelected());
        assertFalse(item3.isSelected());

        // Test désélection
        itemManager.setItemState(false, item2.getUuid());
        assertFalse(item1.isSelected());
        assertFalse(item2.isSelected());
        assertFalse(item3.isSelected());
    }

    @Test
    void setItemStateWithInvalidUuid() {
        // Ajouter des items
        itemManager.addItem(item1);
        itemManager.addItem(item2);

        // Sélectionner d'abord un item
        item1.setSelected(true);
        item2.setSelected(true);

        // Test avec un UUID qui n'existe pas
        UUID nonExistentUuid = UUID.randomUUID();
        itemManager.setItemState(true, nonExistentUuid);

        // Tous les items devraient être désélectionnés car aucun ne correspond
        assertFalse(item1.isSelected());
        assertFalse(item2.isSelected());
    }

    @Test
    void setItemStateOnEmptyList() {
        // Test sur une liste vide
        UUID randomUuid = UUID.randomUUID();

        // Ne devrait pas lever d'exception
        assertDoesNotThrow(() -> {
            itemManager.setItemState(true, randomUuid);
            itemManager.setItemState(false, randomUuid);
        });
    }

    @Test
    void setItemStateEnsuresOnlyOneSelected() {
        // Ajouter plusieurs items
        itemManager.addItem(item1);
        itemManager.addItem(item2);
        itemManager.addItem(item3);

        // Sélectionner manuellement plusieurs items
        item1.setSelected(true);
        item2.setSelected(true);
        item3.setSelected(true);

        // Utiliser setItemState - seul l'item correspondant devrait rester sélectionné
        itemManager.setItemState(true, item2.getUuid());

        assertFalse(item1.isSelected());
        assertTrue(item2.isSelected());
        assertFalse(item3.isSelected());
    }

    @Test
    void constructorInitialization() {
        // Test que le constructeur initialise correctement la liste
        ItemManager newManager = new ItemManager();

        // Test indirect - ajouter un item ne devrait pas lever d'exception
        assertDoesNotThrow(() -> {
            newManager.addItem(new Item());
        });
    }

    @Test
    void addSameItemMultipleTimes() {
        // Test d'ajout du même item plusieurs fois
        itemManager.addItem(item1);
        itemManager.addItem(item1); // Même référence

        // L'item devrait être présent plusieurs fois dans la liste
        itemManager.setItemState(true, item1.getUuid());
        assertTrue(item1.isSelected());

        // Changer l'état devrait affecter toutes les occurrences
        itemManager.setItemState(false, item1.getUuid());
        assertFalse(item1.isSelected());
    }

    @Test
    void addNullItem() {
        // Test d'ajout d'un item null
        assertDoesNotThrow(() -> {
            itemManager.addItem(null);
        });
    }
}
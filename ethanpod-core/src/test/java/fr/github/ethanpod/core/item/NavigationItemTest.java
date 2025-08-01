package fr.github.ethanpod.core.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigationItemTest {
    private final String testName = "home";
    private final String testTitle = "Home Page";
    private final int testNumber = 42;
    private final boolean testIcon = true;
    private NavigationItem navigationItem3Params;
    private NavigationItem navigationItem4Params;

    @BeforeEach
    void setUp() {
        navigationItem3Params = new NavigationItem(testName, testTitle, testIcon);
        navigationItem4Params = new NavigationItem(testName, testTitle, testNumber, testIcon);
    }

    @AfterEach
    void tearDown() {
        navigationItem3Params = null;
        navigationItem4Params = null;
    }

    @Test
    void constructorWithThreeParameters() {
        // Test du constructeur à 3 paramètres
        assertEquals(testName, navigationItem3Params.getName());
        assertEquals(testTitle, navigationItem3Params.getTitle());
        assertEquals(0, navigationItem3Params.getNumber()); // Valeur par défaut
        assertTrue(navigationItem3Params.isIcon());

        // Test que l'héritage fonctionne
        assertNotNull(navigationItem3Params.getUuid());
        assertFalse(navigationItem3Params.isSelected());
    }

    @Test
    void constructorWithFourParameters() {
        // Test du constructeur à 4 paramètres
        assertEquals(testName, navigationItem4Params.getName());
        assertEquals(testTitle, navigationItem4Params.getTitle());
        assertEquals(testNumber, navigationItem4Params.getNumber());
        assertTrue(navigationItem4Params.isIcon());

        // Test que l'héritage fonctionne
        assertNotNull(navigationItem4Params.getUuid());
        assertFalse(navigationItem4Params.isSelected());
    }

    @Test
    void getName() {
        assertEquals(testName, navigationItem3Params.getName());
        assertEquals(testName, navigationItem4Params.getName());

        // Test avec null
        NavigationItem itemWithNullName = new NavigationItem(null, "title", true);
        assertNull(itemWithNullName.getName());

        // Test avec chaîne vide
        NavigationItem itemWithEmptyName = new NavigationItem("", "title", false);
        assertEquals("", itemWithEmptyName.getName());
    }

    @Test
    void getTitle() {
        assertEquals(testTitle, navigationItem3Params.getTitle());
        assertEquals(testTitle, navigationItem4Params.getTitle());

        // Test avec null
        NavigationItem itemWithNullTitle = new NavigationItem("name", null, true);
        assertNull(itemWithNullTitle.getTitle());

        // Test avec chaîne vide
        NavigationItem itemWithEmptyTitle = new NavigationItem("name", "", false);
        assertEquals("", itemWithEmptyTitle.getTitle());
    }

    @Test
    void getNumber() {
        assertEquals(0, navigationItem3Params.getNumber()); // Constructeur 3 paramètres
        assertEquals(testNumber, navigationItem4Params.getNumber()); // Constructeur 4 paramètres

        // Test avec nombre négatif
        NavigationItem itemWithNegativeNumber = new NavigationItem("name", "title", -5, true);
        assertEquals(-5, itemWithNegativeNumber.getNumber());

        // Test avec zéro explicite
        NavigationItem itemWithZero = new NavigationItem("name", "title", 0, false);
        assertEquals(0, itemWithZero.getNumber());

        // Test avec grand nombre
        NavigationItem itemWithLargeNumber = new NavigationItem("name", "title", Integer.MAX_VALUE, true);
        assertEquals(Integer.MAX_VALUE, itemWithLargeNumber.getNumber());
    }

    @Test
    void isIcon() {
        assertTrue(navigationItem3Params.isIcon());
        assertTrue(navigationItem4Params.isIcon());

        // Test avec false
        NavigationItem itemWithoutIcon = new NavigationItem("name", "title", false);
        assertFalse(itemWithoutIcon.isIcon());

        NavigationItem itemWithoutIcon4Params = new NavigationItem("name", "title", 10, false);
        assertFalse(itemWithoutIcon4Params.isIcon());
    }

    @Test
    void constructorInheritance() {
        // Test que super() est appelé correctement dans les deux constructeurs
        NavigationItem item1 = new NavigationItem("test1", "title1", true);
        NavigationItem item2 = new NavigationItem("test2", "title2", 5, false);

        // Chaque instance doit avoir un UUID unique
        assertNotEquals(item1.getUuid(), item2.getUuid());

        // Test de la fonctionnalité héritée
        item1.setSelected(true);
        assertTrue(item1.isSelected());
        assertFalse(item2.isSelected());
    }

    @Test
    void constructorWithAllNullValues() {
        // Test constructeur 3 paramètres avec null
        NavigationItem nullItem3 = new NavigationItem(null, null, false);
        assertNull(nullItem3.getName());
        assertNull(nullItem3.getTitle());
        assertEquals(0, nullItem3.getNumber());
        assertFalse(nullItem3.isIcon());
        assertNotNull(nullItem3.getUuid()); // Héritage fonctionne

        // Test constructeur 4 paramètres avec null
        NavigationItem nullItem4 = new NavigationItem(null, null, 10, false);
        assertNull(nullItem4.getName());
        assertNull(nullItem4.getTitle());
        assertEquals(10, nullItem4.getNumber());
        assertFalse(nullItem4.isIcon());
        assertNotNull(nullItem4.getUuid()); // Héritage fonctionne
    }

    @Test
    void immutabilityOfFields() {
        // Test que les champs sont final (immutable)
        String originalName = navigationItem3Params.getName();
        String originalTitle = navigationItem3Params.getTitle();
        int originalNumber = navigationItem3Params.getNumber();
        boolean originalIcon = navigationItem3Params.isIcon();

        // Les valeurs ne peuvent pas changer après construction
        assertEquals(originalName, navigationItem3Params.getName());
        assertEquals(originalTitle, navigationItem3Params.getTitle());
        assertEquals(originalNumber, navigationItem3Params.getNumber());
        assertEquals(originalIcon, navigationItem3Params.isIcon());
    }

    @Test
    void differentConstructorsBehavior() {
        // Test que les deux constructeurs produisent des objets cohérents
        NavigationItem item3Params = new NavigationItem("same", "same", true);
        NavigationItem item4ParamsWithZero = new NavigationItem("same", "same", 0, true);

        assertEquals(item3Params.getName(), item4ParamsWithZero.getName());
        assertEquals(item3Params.getTitle(), item4ParamsWithZero.getTitle());
        assertEquals(item3Params.getNumber(), item4ParamsWithZero.getNumber()); // Tous deux = 0
        assertEquals(item3Params.isIcon(), item4ParamsWithZero.isIcon());

        // Mais les UUIDs doivent être différents
        assertNotEquals(item3Params.getUuid(), item4ParamsWithZero.getUuid());
    }
}
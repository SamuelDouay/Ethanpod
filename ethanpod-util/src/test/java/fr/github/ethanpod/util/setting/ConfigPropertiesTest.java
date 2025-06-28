package fr.github.ethanpod.util.setting;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Class Config Properties")
class ConfigPropertiesTest {
    private static ConfigProperties configProperties;

    @BeforeAll
    static void init() {
        configProperties = ConfigProperties.getInstance();
    }

    @Test
    @DisplayName("Properties")
    void configProperties() {
        assertEquals("org.sqlite.JDBC", configProperties.getProperty("jdbc.pilot"), "Pilote jdbc");
        assertEquals("#1A2B2C", configProperties.getProperty("light.theme.grey.200"), "Test style properties");
    }

    @Test
    @DisplayName("Properties null")
    void firstTest() {
        assertNull(configProperties.getProperty("light"), "Null properties");
    }
}
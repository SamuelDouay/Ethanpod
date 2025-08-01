package fr.github.ethanpod.core.thread;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTypeTest {
    @Test
    void testAllEnumValues() {
        // Test que toutes les valeurs de l'enum existent
        MessageType[] values = MessageType.values();
        assertEquals(6, values.length);

        // Test de chaque valeur individuellement
        assertEquals(MessageType.REQUEST, MessageType.valueOf("REQUEST"));
        assertEquals(MessageType.RESPONSE, MessageType.valueOf("RESPONSE"));
        assertEquals(MessageType.NOTIFICATION, MessageType.valueOf("NOTIFICATION"));
        assertEquals(MessageType.ERROR, MessageType.valueOf("ERROR"));
        assertEquals(MessageType.DATA_UPDATE, MessageType.valueOf("DATA_UPDATE"));
        assertEquals(MessageType.EVENT, MessageType.valueOf("EVENT"));
    }

    @Test
    void testEnumToString() {
        assertEquals("REQUEST", MessageType.REQUEST.toString());
        assertEquals("RESPONSE", MessageType.RESPONSE.toString());
        assertEquals("NOTIFICATION", MessageType.NOTIFICATION.toString());
        assertEquals("ERROR", MessageType.ERROR.toString());
        assertEquals("DATA_UPDATE", MessageType.DATA_UPDATE.toString());
        assertEquals("EVENT", MessageType.EVENT.toString());
    }

    @Test
    void testEnumOrdinal() {
        assertEquals(0, MessageType.REQUEST.ordinal());
        assertEquals(1, MessageType.RESPONSE.ordinal());
        assertEquals(2, MessageType.NOTIFICATION.ordinal());
        assertEquals(3, MessageType.ERROR.ordinal());
        assertEquals(4, MessageType.DATA_UPDATE.ordinal());
        assertEquals(5, MessageType.EVENT.ordinal());
    }
}
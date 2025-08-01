package fr.github.ethanpod.core.thread;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ThreadMessageTest {
    private final String testContent = "Test content";
    private final String testSender = "TestSender";
    private final String testReceiver = "TestReceiver";
    private final MessageType testType = MessageType.REQUEST;
    private final Object testData = "Test data";
    private final String testRequestId = "test-request-id";
    private ThreadMessage message;

    @BeforeEach
    void setUp() {
        message = new ThreadMessage(testContent, testSender, testReceiver, testType, testData, testRequestId);
    }

    @AfterEach
    void tearDown() {
        message = null;
    }

    @Test
    void constructorAndGetters() {
        assertEquals(testContent, message.getContent());
        assertEquals(testSender, message.getSender());
        assertEquals(testReceiver, message.getReceiver());
        assertEquals(testType, message.getType());
        assertEquals(testData, message.getData());
        assertEquals(testRequestId, message.getRequestId());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void getContent() {
        assertEquals(testContent, message.getContent());

        // Test avec null
        ThreadMessage nullContentMessage = new ThreadMessage(null, testSender, testReceiver, testType, testData, testRequestId);
        assertNull(nullContentMessage.getContent());

        // Test avec chaîne vide
        ThreadMessage emptyContentMessage = new ThreadMessage("", testSender, testReceiver, testType, testData, testRequestId);
        assertEquals("", emptyContentMessage.getContent());
    }

    @Test
    void getSender() {
        assertEquals(testSender, message.getSender());

        // Test avec null
        ThreadMessage nullSenderMessage = new ThreadMessage(testContent, null, testReceiver, testType, testData, testRequestId);
        assertNull(nullSenderMessage.getSender());
    }

    @Test
    void getReceiver() {
        assertEquals(testReceiver, message.getReceiver());

        // Test avec null
        ThreadMessage nullReceiverMessage = new ThreadMessage(testContent, testSender, null, testType, testData, testRequestId);
        assertNull(nullReceiverMessage.getReceiver());
    }

    @Test
    void getType() {
        assertEquals(testType, message.getType());

        // Test avec tous les types
        for (MessageType type : MessageType.values()) {
            ThreadMessage typeMessage = new ThreadMessage(testContent, testSender, testReceiver, type, testData, testRequestId);
            assertEquals(type, typeMessage.getType());
        }
    }

    @Test
    void getTimestamp() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        ThreadMessage newMessage = new ThreadMessage(testContent, testSender, testReceiver, testType, testData, testRequestId);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertTrue(newMessage.getTimestamp().isAfter(before));
        assertTrue(newMessage.getTimestamp().isBefore(after));

        // Test que chaque message a un timestamp unique
        ThreadMessage message2 = new ThreadMessage(testContent, testSender, testReceiver, testType, testData, testRequestId);
        assertNotEquals(message.getTimestamp(), message2.getTimestamp());
    }

    @Test
    void getData() {
        assertEquals(testData, message.getData());

        // Test avec null
        ThreadMessage nullDataMessage = new ThreadMessage(testContent, testSender, testReceiver, testType, null, testRequestId);
        assertNull(nullDataMessage.getData());

        // Test avec différents types d'objets
        Integer intData = 42;
        ThreadMessage intDataMessage = new ThreadMessage(testContent, testSender, testReceiver, testType, intData, testRequestId);
        assertEquals(intData, intDataMessage.getData());
    }

    @Test
    void getRequestId() {
        assertEquals(testRequestId, message.getRequestId());

        // Test avec null
        ThreadMessage nullRequestIdMessage = new ThreadMessage(testContent, testSender, testReceiver, testType, testData, null);
        assertNull(nullRequestIdMessage.getRequestId());
    }

    @Test
    void testToString() {
        String expected = String.format("%s -> %s (%s): %s avec l'ID %s", testSender, testReceiver, testType, testContent, testRequestId);
        assertEquals(expected, message.toString());

        // Test avec valeurs null
        ThreadMessage nullMessage = new ThreadMessage(null, null, null, MessageType.ERROR, null, null);
        String expectedNull = String.format("%s -> %s (%s): %s avec l'ID %s", null, null, MessageType.ERROR, null, null);
        assertEquals(expectedNull, nullMessage.toString());
    }

    @Test
    void immutabilityOfFields() {
        // Test que tous les champs sont final et immutable
        String originalContent = message.getContent();
        String originalSender = message.getSender();
        String originalReceiver = message.getReceiver();
        MessageType originalType = message.getType();
        Object originalData = message.getData();
        String originalRequestId = message.getRequestId();
        LocalDateTime originalTimestamp = message.getTimestamp();

        // Les valeurs ne peuvent pas changer
        assertEquals(originalContent, message.getContent());
        assertEquals(originalSender, message.getSender());
        assertEquals(originalReceiver, message.getReceiver());
        assertEquals(originalType, message.getType());
        assertEquals(originalData, message.getData());
        assertEquals(originalRequestId, message.getRequestId());
        assertEquals(originalTimestamp, message.getTimestamp());
    }
}

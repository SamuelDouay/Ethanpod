package fr.github.ethanpod.core.thread;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageRouterTest {

    private final String testThreadName = "TestThread";
    private MessageRouter messageRouter;
    private BlockingQueue<ThreadMessage> testQueue;

    @BeforeEach
    void setUp() {
        messageRouter = new MessageRouter();
        testQueue = new LinkedBlockingQueue<>();
    }

    @AfterEach
    void tearDown() {
        messageRouter = null;
        testQueue = null;
    }

    @Test
    void getInstance() {
        MessageRouter instance1 = MessageRouter.getInstance();
        MessageRouter instance2 = MessageRouter.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2); // Test du singleton
    }

    @Test
    void registerThreadWithQueue() {
        messageRouter.registerThread(testThreadName, testQueue);

        // Test indirect via routeMessage
        ThreadMessage message = new ThreadMessage("test", "sender", testThreadName, MessageType.REQUEST, null, "id");
        assertTrue(messageRouter.routeMessage(message));

        // Vérifier que le message est dans la queue
        assertEquals(1, testQueue.size());
        assertEquals(message, testQueue.poll());
    }

    @Test
    void registerThreadWithoutQueue() {
        BlockingQueue<ThreadMessage> returnedQueue = messageRouter.registerThread(testThreadName);

        assertNotNull(returnedQueue);
        assertTrue(returnedQueue instanceof LinkedBlockingQueue);

        // Test que la queue est bien enregistrée
        ThreadMessage message = new ThreadMessage("test", "sender", testThreadName, MessageType.REQUEST, null, "id");
        assertTrue(messageRouter.routeMessage(message));

        assertEquals(1, returnedQueue.size());
        assertEquals(message, returnedQueue.poll());
    }

    @Test
    void routeMessageSuccess() {
        messageRouter.registerThread(testThreadName, testQueue);

        ThreadMessage message = new ThreadMessage("test content", "sender", testThreadName, MessageType.NOTIFICATION, "data", "req-id");

        assertTrue(messageRouter.routeMessage(message));
        assertEquals(1, testQueue.size());
        assertEquals(message, testQueue.poll());
    }

    @Test
    void routeMessageTargetNotFound() {
        ThreadMessage message = new ThreadMessage("test", "sender", "NonExistentThread", MessageType.REQUEST, null, "id");

        assertFalse(messageRouter.routeMessage(message));
    }

    @Test
    void routeMessageWithRequestType() {
        messageRouter.registerThread(testThreadName, testQueue);

        String requestId = "test-request-id";
        ThreadMessage requestMessage = new ThreadMessage("request", "sender", testThreadName, MessageType.REQUEST, null, requestId);

        assertTrue(messageRouter.routeMessage(requestMessage));
        assertEquals(1, testQueue.size());
    }

    @Test
    void routeMessageWithResponseType() {
        messageRouter.registerThread(testThreadName, testQueue);

        // D'abord envoyer une requête pour tracer l'expéditeur
        String requestId = "test-request-id";
        ThreadMessage requestMessage = new ThreadMessage("request", "originalSender", testThreadName, MessageType.REQUEST, null, requestId);
        messageRouter.routeMessage(requestMessage);
        testQueue.clear(); // Nettoyer pour le test de réponse

        // Ensuite envoyer une réponse
        ThreadMessage responseMessage = new ThreadMessage("response", testThreadName, "originalSender", MessageType.RESPONSE, null, requestId);
        messageRouter.registerThread("originalSender", new LinkedBlockingQueue<>());

        assertTrue(messageRouter.routeMessage(responseMessage));
    }

    @Test
    void routeMessageWithNullRequestId() {
        messageRouter.registerThread(testThreadName, testQueue);

        ThreadMessage message = new ThreadMessage("test", "sender", testThreadName, MessageType.REQUEST, null, null);

        assertTrue(messageRouter.routeMessage(message));
        assertEquals(1, testQueue.size());
    }

    @Test
    void routeMessageInterruptedException() {
        // Créer une queue mockée qui lance InterruptedException
        BlockingQueue<ThreadMessage> mockQueue = mock(BlockingQueue.class);

        try {
            doThrow(new InterruptedException("Test interruption")).when(mockQueue).put(any(ThreadMessage.class));
        } catch (InterruptedException _) {
            fail("Setup failed");
        }

        messageRouter.registerThread(testThreadName, mockQueue);

        ThreadMessage message = new ThreadMessage("test", "sender", testThreadName, MessageType.REQUEST, null, "id");

        assertFalse(messageRouter.routeMessage(message));
        assertTrue(Thread.currentThread().isInterrupted());

        // Reset interrupt flag for cleanup
        Thread.interrupted();
    }

    @Test
    void sendRequestToLogicFromView() {
        BlockingQueue<ThreadMessage> logicQueue = messageRouter.registerThread(MessageRouter.LOGIC_THREAD);

        String request = "test request";
        String requestId = "test-id";
        MessageType messageType = MessageType.REQUEST;
        Object data = "test data";

        messageRouter.sendRequestToLogicFromView(request, requestId, messageType, data);

        assertEquals(1, logicQueue.size());
        ThreadMessage message = logicQueue.poll();
        assertEquals(request, message.getContent());
        assertEquals(MessageRouter.VIEW_THREAD, message.getSender());
        assertEquals(MessageRouter.LOGIC_THREAD, message.getReceiver());
        assertEquals(messageType, message.getType());
        assertEquals(data, message.getData());
        assertEquals(requestId, message.getRequestId());
    }

    @Test
    void sendRequestToViewFromLogic() {
        BlockingQueue<ThreadMessage> viewQueue = messageRouter.registerThread(MessageRouter.VIEW_THREAD);

        String request = "test request";
        String requestId = "test-id";
        MessageType messageType = MessageType.RESPONSE;
        Object data = "test data";

        messageRouter.sendRequestToViewFromLogic(request, requestId, messageType, data);

        assertEquals(1, viewQueue.size());
        ThreadMessage message = viewQueue.poll();
        assertEquals(request, message.getContent());
        assertEquals(MessageRouter.LOGIC_THREAD, message.getSender());
        assertEquals(MessageRouter.VIEW_THREAD, message.getReceiver());
        assertEquals(messageType, message.getType());
        assertEquals(data, message.getData());
        assertEquals(requestId, message.getRequestId());
    }

    @Test
    void sendRequestToViewFromEvent() {
        BlockingQueue<ThreadMessage> viewQueue = messageRouter.registerThread(MessageRouter.VIEW_THREAD);

        String request = "test request";
        String requestId = "test-id";
        MessageType messageType = MessageType.EVENT;
        Object data = "test data";

        messageRouter.sendRequestToViewFromEvent(request, requestId, messageType, data);

        assertEquals(1, viewQueue.size());
        ThreadMessage message = viewQueue.poll();
        assertEquals(request, message.getContent());
        assertEquals(MessageRouter.UI_EVENT_THREAD, message.getSender());
        assertEquals(MessageRouter.VIEW_THREAD, message.getReceiver());
        assertEquals(messageType, message.getType());
        assertEquals(data, message.getData());
        assertEquals(requestId, message.getRequestId());
    }

    @Test
    void sendRequestToUiEventFromView() {
        BlockingQueue<ThreadMessage> uiEventQueue = messageRouter.registerThread(MessageRouter.UI_EVENT_THREAD);

        String request = "test request";
        String requestId = "test-id";
        MessageType messageType = MessageType.NOTIFICATION;
        Object data = "test data";

        messageRouter.sendRequestToUiEventFromView(request, requestId, messageType, data);

        assertEquals(1, uiEventQueue.size());
        ThreadMessage message = uiEventQueue.poll();
        assertEquals(request, message.getContent());
        assertEquals(MessageRouter.VIEW_THREAD, message.getSender());
        assertEquals(MessageRouter.UI_EVENT_THREAD, message.getReceiver());
        assertEquals(messageType, message.getType());
        assertEquals(data, message.getData());
        assertEquals(requestId, message.getRequestId());
    }

    @Test
    void sendRequestWithNullRequestId() {
        BlockingQueue<ThreadMessage> logicQueue = messageRouter.registerThread(MessageRouter.LOGIC_THREAD);

        // Mock UUID.randomUUID() pour tester la génération automatique d'ID
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            UUID mockUuid = mock(UUID.class);
            when(mockUuid.toString()).thenReturn("generated-uuid");
            mockedUUID.when(UUID::randomUUID).thenReturn(mockUuid);

            messageRouter.sendRequestToLogicFromView("test", null, MessageType.REQUEST, null);

            assertEquals(1, logicQueue.size());
            ThreadMessage message = logicQueue.poll();
            assertEquals("generated-uuid", message.getRequestId());
        }
    }

    @Test
    void constantsValues() {
        assertEquals("UIEventThread", MessageRouter.UI_EVENT_THREAD);
        assertEquals("ViewThread", MessageRouter.VIEW_THREAD);
        assertEquals("LogicThread", MessageRouter.LOGIC_THREAD);
    }

    @Test
    void constructorInitialization() {
        MessageRouter newRouter = new MessageRouter();
        assertNotNull(newRouter);

        // Test que les collections sont initialisées
        BlockingQueue<ThreadMessage> queue = newRouter.registerThread("test");
        assertNotNull(queue);
    }

    @Test
    void multipleThreadsRegistration() {
        BlockingQueue<ThreadMessage> queue1 = messageRouter.registerThread("Thread1");
        BlockingQueue<ThreadMessage> queue2 = messageRouter.registerThread("Thread2");
        BlockingQueue<ThreadMessage> queue3 = new LinkedBlockingQueue<>();
        messageRouter.registerThread("Thread3", queue3);

        assertNotNull(queue1);
        assertNotNull(queue2);
        assertNotSame(queue1, queue2);

        // Test que chaque thread reçoit ses messages
        ThreadMessage msg1 = new ThreadMessage("msg1", "sender", "Thread1", MessageType.REQUEST, null, "id1");
        ThreadMessage msg2 = new ThreadMessage("msg2", "sender", "Thread2", MessageType.REQUEST, null, "id2");
        ThreadMessage msg3 = new ThreadMessage("msg3", "sender", "Thread3", MessageType.REQUEST, null, "id3");

        assertTrue(messageRouter.routeMessage(msg1));
        assertTrue(messageRouter.routeMessage(msg2));
        assertTrue(messageRouter.routeMessage(msg3));

        assertEquals(1, queue1.size());
        assertEquals(1, queue2.size());
        assertEquals(1, queue3.size());

        assertEquals(msg1, queue1.poll());
        assertEquals(msg2, queue2.poll());
        assertEquals(msg3, queue3.poll());
    }

    @Test
    void requestResponseFlow() {
        BlockingQueue<ThreadMessage> senderQueue = messageRouter.registerThread("Sender");
        BlockingQueue<ThreadMessage> receiverQueue = messageRouter.registerThread("Receiver");

        String requestId = "flow-test-id";

        // Envoyer une requête
        ThreadMessage request = new ThreadMessage("request", "Sender", "Receiver", MessageType.REQUEST, null, requestId);
        assertTrue(messageRouter.routeMessage(request));

        assertEquals(1, receiverQueue.size());
        ThreadMessage receivedRequest = receiverQueue.poll();
        assertEquals(request, receivedRequest);

        // Envoyer une réponse
        ThreadMessage response = new ThreadMessage("response", "Receiver", "Sender", MessageType.RESPONSE, null, requestId);
        assertTrue(messageRouter.routeMessage(response));

        assertEquals(1, senderQueue.size());
        ThreadMessage receivedResponse = senderQueue.poll();
        assertEquals(response, receivedResponse);
    }
}
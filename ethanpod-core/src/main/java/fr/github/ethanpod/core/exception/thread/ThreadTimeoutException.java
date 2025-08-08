package fr.github.ethanpod.core.exception.thread;

import fr.github.ethanpod.core.exception.EthanpodException;

public class ThreadTimeoutException extends EthanpodException {

    private final long timeoutMs;

    public ThreadTimeoutException(String message, long timeoutMs) {
        super("THREAD_TIMEOUT", message, timeoutMs);
        this.timeoutMs = timeoutMs;
    }

    public static ThreadTimeoutException requestTimeout(String requestId, long timeoutMs) {
        return new ThreadTimeoutException(
                String.format("Timeout de la requête %s après %dms", requestId, timeoutMs),
                timeoutMs
        );
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }
}

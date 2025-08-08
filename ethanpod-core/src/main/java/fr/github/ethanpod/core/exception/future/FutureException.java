package fr.github.ethanpod.core.exception.future;

import fr.github.ethanpod.core.exception.EthanpodException;

public class FutureException extends EthanpodException {
    public FutureException(String message) {
        super("FUTURE_ERROR", message);
    }

    public FutureException(String message, Throwable cause) {
        super("FUTURE_ERROR", message, cause);
    }

    public static FutureException futureNotFound(String id) {
        return new FutureException("Future not found for request " + id);
    }
}

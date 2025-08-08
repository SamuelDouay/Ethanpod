package fr.github.ethanpod.core.exception.technical;

import fr.github.ethanpod.core.exception.EthanpodException;

public class ConnectionPoolException extends EthanpodException {

    public ConnectionPoolException(String message) {
        super("POOL_ERROR", message);
    }

    public ConnectionPoolException(String message, Throwable cause) {
        super("POOL_ERROR", message, cause);
    }

    public static ConnectionPoolException poolExhausted() {
        return new ConnectionPoolException("Pool de connexions épuisé");
    }

    public static ConnectionPoolException configurationError(String message) {
        return new ConnectionPoolException("Erreur de configuration du pool: " + message);
    }
}
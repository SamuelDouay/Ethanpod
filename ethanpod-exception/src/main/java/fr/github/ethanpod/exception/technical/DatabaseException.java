package fr.github.ethanpod.exception.technical;

import fr.github.ethanpod.exception.EthanpodException;

public class DatabaseException extends EthanpodException {

    public DatabaseException(String message) {
        super("DB_ERROR", message);
    }

    public DatabaseException(String message, Throwable cause) {
        super("DB_ERROR", message, cause);
    }

    public static DatabaseException connectionFailed(String url, Throwable cause) {
        return new DatabaseException("Échec de connexion à la base de données: " + url, cause);
    }

    public static DatabaseException queryFailed(String query, Throwable cause) {
        return new DatabaseException("Échec d'exécution de la requête: " + query, cause);
    }
}
package fr.github.ethanpod.exception.util;

import fr.github.ethanpod.exception.technical.ConnectionPoolException;
import fr.github.ethanpod.exception.technical.DatabaseException;

import java.sql.SQLException;

public class ExceptionConverter {
    private ExceptionConverter() {
        // no param
    }

    public static DatabaseException fromSQLException(SQLException sqlEx, String context) {
        return DatabaseException.queryFailed(context, sqlEx);
    }

    public static ConnectionPoolException fromPoolException(Exception poolEx) {
        return new ConnectionPoolException("Erreur du pool de connexions", poolEx);
    }

    public static RuntimeException wrapUnexpected(Exception ex, String context) {
        return new RuntimeException("Erreur inattendue dans " + context, ex);
    }
}

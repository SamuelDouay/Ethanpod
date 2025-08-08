package fr.github.ethanpod.core.exception.util;

import fr.github.ethanpod.core.exception.technical.ConnectionPoolException;
import fr.github.ethanpod.core.exception.technical.DatabaseException;

import java.sql.SQLException;

public class ExceptionConverter {
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

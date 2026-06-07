package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.exception.technical.DatabaseException;
import fr.github.ethanpod.logic.sql.query.SqlQueryBuilder;
import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {
    protected static final Logger LOGGER = LogManager.getLogger(BaseDao.class);


    private final DatabaseManager databaseManager;

    protected BaseDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }


    private void logMetrics(String sql, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        if (executionTime > 100) {
            LOGGER.warn("Slow SQL Query detected: {} executed in {}ms", sql, executionTime);
        } else {
            LOGGER.debug("SQL Query executed in {}ms: {}", executionTime, sql);
        }
    }

    protected <T> T executeQuery(SqlQueryBuilder sqlQueryBuilder, ResultSetMapper<T> mapper, T defaultValue, String context) {
        long startTime = System.currentTimeMillis();
        String sql = sqlQueryBuilder.build();
        try {
            return databaseManager.executeWithConnection(conn -> {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    T result = mapper.map(rs);
                    logMetrics(sql, startTime);
                    return result;

                } catch (SQLException e) {
                    LOGGER.error("Erreur SQL [{}]: {}", sql, e.getMessage(), e);
                    return defaultValue;
                }
            }, context);

        } catch (DatabaseException e) {
            LOGGER.error("Erreur base de données lors de [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected <T> T executeQueryWithParams(SqlQueryBuilder sqlQueryBuilder, ResultSetMapper<T> mapper, T defaultValue, String context) {
        long startTime = System.currentTimeMillis();
        String sql = sqlQueryBuilder.build();
        Object[] params = sqlQueryBuilder.getParameters();
        try {
            return databaseManager.executeWithConnection(conn -> {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }

                    try (ResultSet rs = stmt.executeQuery()) {
                        T result = mapper.map(rs);
                        logMetrics(sql, startTime);
                        return result;
                    }
                }
            }, context);

        } catch (DatabaseException e) {
            LOGGER.error("Erreur lors de la requête [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected int executeUpdate(SqlQueryBuilder sqlQueryBuilder, String context) {
        long startTime = System.currentTimeMillis();
        String sql = sqlQueryBuilder.build();
        Object[] params = sqlQueryBuilder.getParameters();
        try {
            return databaseManager.executeWithConnection(conn -> {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    // Paramètres
                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);
                    }

                    int rowsAffected = stmt.executeUpdate();
                    logMetrics(sql, startTime);
                    return rowsAffected;
                }
            }, context);

        } catch (DatabaseException e) {
            LOGGER.error("Erreur lors de la requête [{}]: {}", sql, e.getMessage(), e);
            return 0;
        }
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}

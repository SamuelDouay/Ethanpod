package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.logic.sql.setting.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {
    protected static final Logger logger = LogManager.getLogger(BaseDao.class);

    private void logMetrics(String sql, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        if (executionTime > 100) {
            logger.warn("Slow SQL Query detected: {} executed in {}ms", sql, executionTime);
        } else {
            logger.debug("SQL Query executed in {}ms: {}", executionTime, sql);
        }
    }

    private Connection getConnexion() throws SQLException {
        if (!DatabaseManager.getInstance().isInitialized()) {
            DatabaseManager.getInstance().initialize();
        }
        return DatabaseManager.getInstance().getConnection();
    }

    protected <T> T executeQuery(String sql, ResultSetMapper<T> mapper, T defaultValue) {
        long startTime = System.currentTimeMillis();
        try (PreparedStatement stmt = getConnexion().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            T result = mapper.map(rs);
            logMetrics(sql, startTime);
            return result;

        } catch (SQLException e) {
            logger.error("Erreur SQL [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected <T> T executeQueryWithParams(String sql, ResultSetMapper<T> mapper, Object... params) {
        return executeQueryWithParams(sql, mapper, null, params);
    }

    protected <T> T executeQueryWithParams(String sql, ResultSetMapper<T> mapper, T defaultValue, Object... params) {
        long startTime = System.currentTimeMillis();
        try (PreparedStatement stmt = getConnexion().prepareStatement(sql)) {

            // Paramètres
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                T result = mapper.map(rs);

                logMetrics(sql, startTime);
                return result;
            }

        } catch (SQLException e) {
            logger.error("Erreur SQL avec params [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected int executeUpdate(String sql, Object... params) {
        long startTime = System.currentTimeMillis();
        try (PreparedStatement stmt = getConnexion().prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            int rowsAffected = stmt.executeUpdate();
            logMetrics(sql, startTime);
            return rowsAffected;

        } catch (SQLException e) {
            logger.error("Erreur update SQL [{}]: {}", sql, e.getMessage(), e);
            return 0;
        }
    }

    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}

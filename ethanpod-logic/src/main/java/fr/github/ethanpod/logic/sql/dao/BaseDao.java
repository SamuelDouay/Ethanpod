package fr.github.ethanpod.logic.sql.dao;

import fr.github.ethanpod.logic.sql.setting.Connect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {
    protected static final Logger logger = LogManager.getLogger(BaseDao.class);

    // Méthodes utilitaires génériques - une fois écrites, utilisées partout
    protected <T> T executeQuery(String sql, ResultSetMapper<T> mapper) {
        return executeQuery(sql, mapper, null);
    }

    protected <T> T executeQuery(String sql, ResultSetMapper<T> mapper, T defaultValue) {
        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.map(rs);

        } catch (SQLException e) {
            logger.error("Erreur SQL [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected <T> T executeQueryWithParams(String sql, ResultSetMapper<T> mapper, Object... params) {
        return executeQueryWithParams(sql, mapper, null, params);
    }

    protected <T> T executeQueryWithParams(String sql, ResultSetMapper<T> mapper, T defaultValue, Object... params) {
        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql)) {

            // Paramètres
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                return mapper.map(rs);
            }

        } catch (SQLException e) {
            logger.error("Erreur SQL avec params [{}]: {}", sql, e.getMessage(), e);
            return defaultValue;
        }
    }

    protected int executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = Connect.getInstance().getConnection().prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();

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

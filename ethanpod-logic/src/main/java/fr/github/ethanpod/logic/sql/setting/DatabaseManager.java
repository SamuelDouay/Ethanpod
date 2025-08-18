package fr.github.ethanpod.logic.sql.setting;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.github.ethanpod.exception.EthanpodRuntimeException;
import fr.github.ethanpod.exception.technical.ConnectionPoolException;
import fr.github.ethanpod.exception.technical.DatabaseException;
import fr.github.ethanpod.exception.util.ExceptionConverter;
import fr.github.ethanpod.util.setting.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger();
    private HikariDataSource dataSource;
    private boolean initialized = false;
    private boolean initializing = false;

    public DatabaseManager() {
        // no param
    }

    public synchronized void initialize() {
        if (initialized) {
            logger.warn("DatabaseManager déjà initialisé");
            return;
        }
        synchronized (DatabaseManager.class) {
            if (initialized) return;
            initializing = true;

            try {
                loadSQLiteDriver();
                String jdbcUrl = buildJdbcUrl();
                this.dataSource = createDataSource(jdbcUrl);
                testConnection(); // ← autorisé car initializing = true
                registerShutdownHook();
                this.initialized = true;
                logger.debug("DatabaseManager initialisé avec succès");
            } catch (DatabaseException | ConnectionPoolException e) {
                cleanup();
                logger.error("Erreur lors de l'initialisation du DatabaseManager: {}", e.getMessage(), e);
                throw EthanpodRuntimeException.systemError("Database initialization failed", e);
            } finally {
                initializing = false;
            }
        }
    }

    private void loadSQLiteDriver() throws DatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
            logger.debug("Driver SQLite chargé avec succès");
        } catch (ClassNotFoundException e) {
            logger.error("Driver SQLite non trouvé dans le classpath");
            throw new DatabaseException("Driver SQLite non disponible", e);
        }
    }

    private String buildJdbcUrl() {
        try {
            String jdbcPrefix = ConfigProperties.getInstance().getProperty("jdbc.database");
            if (jdbcPrefix == null || jdbcPrefix.trim().isEmpty()) {
                throw EthanpodRuntimeException.configurationError("Propriété 'jdbc.database' manquante ou vide");
            }

            URL dbResource = DatabaseManager.class.getResource("/data/data_140825.db");
            if (dbResource == null) {
                throw EthanpodRuntimeException.configurationError("Fichier de base de données non trouvé: /data/data_140825.db");
            }

            String jdbcUrl = jdbcPrefix + dbResource;
            logger.debug("URL JDBC construite: {}", jdbcUrl);
            return jdbcUrl;

        } catch (Exception e) {
            throw EthanpodRuntimeException.configurationError("Erreur lors de la construction de l'URL JDBC: " + e.getMessage());
        }
    }

    private HikariDataSource createDataSource(String jdbcUrl) throws ConnectionPoolException {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setDriverClassName("org.sqlite.JDBC");

            // Configuration optimisée pour SQLite
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setConnectionTimeout(5000);
            config.setIdleTimeout(300000);
            config.setMaxLifetime(1800000); // 30 minutes
            config.setKeepaliveTime(120000);
            config.setPoolName("SQLitePool");

            // Propriétés SQLite pour performance
            config.addDataSourceProperty("journal_mode", "WAL");
            config.addDataSourceProperty("synchronous", "NORMAL");
            config.addDataSourceProperty("cache_size", "10000");
            config.addDataSourceProperty("foreign_keys", "true");
            config.addDataSourceProperty("busy_timeout", "5000");
            config.addDataSourceProperty("temp_store", "MEMORY");

            logger.debug("Configuration HikariCP créée pour SQLite");
            return new HikariDataSource(config);

        } catch (Exception e) {
            logger.error("Erreur lors de la création du DataSource: {}", e.getMessage(), e);
            throw ConnectionPoolException.configurationError("Échec de la configuration HikariCP: " + e.getMessage());
        }
    }

    private void testConnection() throws DatabaseException {
        try (Connection testConnection = getConnection()) {
            if (!testConnection.isValid(5)) {
                throw new DatabaseException("Validation de connexion échouée - connexion invalide");
            }
            logger.debug("Test de connexion réussi");

        } catch (SQLException e) {
            logger.error("Test de connexion échoué: {}", e.getMessage(), e);
            throw ExceptionConverter.fromSQLException(e, "test de connexion");

        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors du test de connexion: {}", e.getMessage(), e);
            throw new DatabaseException("Erreur inattendue lors du test de connexion", e);
        }
    }

    public Connection getConnection() throws DatabaseException {
        if ((!initialized && !initializing) || dataSource == null) {
            throw new DatabaseException("DatabaseManager non initialisé - appelez initialize() d'abord");
        }

        if (dataSource.isClosed()) {
            throw new DatabaseException("DataSource fermé - impossible d'obtenir une connexion");
        }

        try {
            Connection connection = dataSource.getConnection();
            if (connection == null) {
                throw new DatabaseException("Le pool a retourné une connexion null");
            }
            return connection;

        } catch (SQLException e) {
            logger.error("Erreur lors de l'obtention d'une connexion: {}", e.getMessage(), e);

            // Vérifier si c'est un problème de pool épuisé
            if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                throw new DatabaseException("Timeout lors de l'obtention d'une connexion - pool épuisé", e);
            }

            throw ExceptionConverter.fromSQLException(e, "obtention de connexion");
        }
    }

    public String getPoolStats() {
        if (dataSource != null && !dataSource.isClosed()) {
            return String.format("Pool Stats - Active: %d, Idle: %d, Total: %d, Waiting: %d",
                    dataSource.getHikariPoolMXBean().getActiveConnections(),
                    dataSource.getHikariPoolMXBean().getIdleConnections(),
                    dataSource.getHikariPoolMXBean().getTotalConnections(),
                    dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
            );
        }
        return "Pool non disponible";
    }

    public synchronized void shutdown() {
        cleanup();
    }

    private void cleanup() {
        if (dataSource != null && !dataSource.isClosed()) {
            try {
                logger.debug("Fermeture du pool de connexions: {}", getPoolStats());
                dataSource.close();
                logger.debug("Pool de connexions fermé avec succès");
            } catch (Exception e) {
                logger.warn("Erreur lors de la fermeture du DataSource: {}", e.getMessage());
            }
        }
        initialized = false;
        dataSource = null;
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.debug("Shutdown hook - Fermeture du DatabaseManager");
            shutdown();
        }));
    }

    public <T> T executeWithConnection(DatabaseAction<T> action, String context) throws DatabaseException {
        try (Connection conn = getConnection()) {
            return action.execute(conn);
        } catch (SQLException e) {
            throw ExceptionConverter.fromSQLException(e, context);
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Erreur inattendue lors de " + context, e);
        }
    }

    @FunctionalInterface
    public interface DatabaseAction<T> {
        T execute(Connection connection) throws SQLException, DatabaseException;
    }
}

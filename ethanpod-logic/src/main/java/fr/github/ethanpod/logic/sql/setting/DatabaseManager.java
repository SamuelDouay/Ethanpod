package fr.github.ethanpod.logic.sql.setting;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

    private DatabaseManager() {
        // no param
    }

    public static DatabaseManager getInstance() {
        return DatabaseManager.Holder.instance;
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
                logger.info("DatabaseManager initialisé avec succès");
            } catch (Exception e) {
                cleanup();
                logger.error("Erreur lors de l'initialisation du DatabaseManager: {}", e.getMessage(), e);
                throw new RuntimeException("Database initialization failed", e);
            } finally {
                initializing = false;
            }
        }
    }

    private void loadSQLiteDriver() throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            logger.debug("Driver SQLite chargé avec succès");
        } catch (ClassNotFoundException e) {
            logger.error("Driver SQLite non trouvé dans le classpath");
            throw e;
        }
    }

    private String buildJdbcUrl() {
        String jdbcPrefix = ConfigProperties.getInstance().getProperty("jdbc.database");
        
        URL dbResource = DatabaseManager.class.getResource("/data/data_240825.db");
        if (dbResource == null) {
            throw new RuntimeException("Fichier de base de données non trouvé: /data/data_240825.db");
        }

        String jdbcUrl = jdbcPrefix + dbResource;

        logger.info("URL JDBC construite: {}", jdbcUrl);
        return jdbcUrl;
    }

    private HikariDataSource createDataSource(String jdbcUrl) {
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
    }

    private void testConnection() throws SQLException {
        try (Connection testConnection = getConnection()) {
            if (!testConnection.isValid(5)) {
                throw new SQLException("Validation de connexion échouée");
            }
            logger.info("Test de connexion réussi");
        }
    }

    public Connection getConnection() throws SQLException {
        if ((!initialized && !initializing) || dataSource == null) {
            throw new IllegalStateException("DatabaseManager non initialisé. Appelez initialize() d'abord.");
        }

        if (dataSource.isClosed()) {
            throw new SQLException("DataSource fermé");
        }

        return dataSource.getConnection();
    }

    public boolean isInitialized() {
        return initialized && dataSource != null && !dataSource.isClosed();
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
                logger.info("Fermeture du pool de connexions: {}", getPoolStats());
                dataSource.close();
                logger.info("Pool de connexions fermé avec succès");
            } catch (Exception e) {
                logger.warn("Erreur lors de la fermeture du DataSource: {}", e.getMessage());
            }
        }
        initialized = false;
        dataSource = null;
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook - Fermeture du DatabaseManager");
            shutdown();
        }));
    }

    private static final class Holder {
        private static final DatabaseManager instance = new DatabaseManager();
    }
}

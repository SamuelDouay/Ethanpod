package fr.github.ethanpod.logic.sql.setting;

import fr.github.ethanpod.util.setting.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    private static final Logger logger = LogManager.getLogger();
    private Connection connection;

    private Connect() {
        try {
            String database = String.valueOf(Connect.class.getResource("/data/data_240825.db"));
            this.connection = DriverManager.getConnection(ConfigProperties.getInstance().getProperty("jdbc.database") + database);
        } catch (Exception e) {
            logger.error("{}: {}", e.getClass().getName(), e.getMessage());
            System.exit(0);
        }
        logger.info("Opened database successfully");

    }

    public static Connect getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    private static class Holder {
        private static final Connect INSTANCE = new Connect();
    }
}

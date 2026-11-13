package com.skillswap.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages database connectivity using HikariCP connection pooling.
 * Connects to MySQL 'skillswap_db'.
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static HikariDataSource dataSource;

    static {
        initDataSource();
    }

    private static synchronized void initDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }
        try {
            Properties props = new Properties();
            try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is != null) {
                    props.load(is);
                } else {
                    LOGGER.warning("db.properties not found on classpath, using defaults.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to load db.properties", e);
            }

            String driver = System.getProperty("db.driver", props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
            String url = System.getenv("DB_URL") != null ? System.getenv("DB_URL") :
                         System.getProperty("db.url", props.getProperty("db.url",
                         "jdbc:mysql://localhost:3306/skillswap_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"));
            String username = System.getenv("DB_USER") != null ? System.getenv("DB_USER") :
                             System.getProperty("db.username", props.getProperty("db.username", "root"));
            String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") :
                             System.getProperty("db.password", props.getProperty("db.password", ""));

            int maxPool = Integer.parseInt(props.getProperty("db.pool.maxSize", "10"));
            int minIdle = Integer.parseInt(props.getProperty("db.pool.minIdle", "2"));
            long idleTimeout = Long.parseLong(props.getProperty("db.pool.idleTimeout", "30000"));
            long connTimeout = Long.parseLong(props.getProperty("db.pool.connectionTimeout", "10000"));

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(maxPool);
            config.setMinimumIdle(minIdle);
            config.setIdleTimeout(idleTimeout);
            config.setConnectionTimeout(connTimeout);
            config.setPoolName("SkillSwapHikariPool");

            // Performance optimizations
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            dataSource = new HikariDataSource(config);
            LOGGER.info("HikariCP connection pool initialized successfully for " + url);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize HikariCP DataSource: " + e.getMessage(), e);
        }
    }

    /**
     * Obtains a connection from the connection pool.
     * @return java.sql.Connection
     * @throws SQLException if a connection cannot be acquired
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initDataSource();
        }
        if (dataSource == null) {
            throw new SQLException("Database pool is not initialized. Please check MySQL server and db.properties.");
        }
        return dataSource.getConnection();
    }

    /**
     * Safely closes one or more JDBC auto-closeable resources without throwing checked exceptions.
     */
    public static void closeQuietly(AutoCloseable... closeables) {
        if (closeables == null) return;
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error closing resource", e);
                }
            }
        }
    }

    /**
     * Shutdown the connection pool (invoked on servlet context destroyed).
     */
    public static synchronized void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("HikariCP connection pool shut down cleanly.");
        }
    }
}


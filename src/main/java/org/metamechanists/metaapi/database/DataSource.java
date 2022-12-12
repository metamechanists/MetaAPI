/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    private static javax.sql.DataSource dataSource;

    public static javax.sql.DataSource getDataSource() {
        if (dataSource == null) {
            DatabaseSettings settings = DatabaseSettings.getInstance();
            HikariConfig config = new HikariConfig();
            config.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            config.addDataSourceProperty("databaseName", settings.getDatabase());
            config.addDataSourceProperty("serverName", settings.getAddress());
            config.addDataSourceProperty("port", settings.getPort());
            config.setUsername(settings.getUsername());
            config.setPassword(settings.getPassword());
            config.setMaximumPoolSize(settings.getConnectionPoolSize());
            config.setMinimumIdle(5);
            config.setIdleTimeout(60000);
            config.setConnectionTimeout(30000);
            config.setValidationTimeout(5000);
            config.setMaxLifetime(1800000);
            config.setConnectionTestQuery("SELECT 1");
            config.setPoolName("metaapi-hikari");
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}

/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.database;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.metamechanists.metaapi.config.ConfigUtil;
import org.metamechanists.metaapi.config.ResourceLoader;

public class DatabaseSettings {

    private static DatabaseSettings instance;

    @Getter
    private final String database;
    @Getter
    private final String address;
    @Getter
    private final int port;
    @Getter
    private final String username;
    @Getter
    private final String password;
    @Getter
    private final int connectionPoolSize;

    private DatabaseSettings() {
        FileConfiguration config = ResourceLoader.getConfig();
        database = ConfigUtil.getString("config", config, "database.database");
        address = ConfigUtil.getString("config", config,"database.address");
        port = ConfigUtil.getInt(config,"database.port");
        username = ConfigUtil.getString("config", config,"database.username");
        password = ConfigUtil.getString("config", config,"database.password");
        connectionPoolSize = ConfigUtil.getInt(config,"database.connection-pool-size");
    }

    public static DatabaseSettings getInstance() {
        if (instance == null) {
            instance = new DatabaseSettings();
        }
        return instance;
    }
}


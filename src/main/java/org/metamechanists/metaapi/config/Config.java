package org.metamechanists.metaapi.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private static final String NAME = "config";
    @Getter
    private static String stage;

    public static void initialize() {
        FileConfiguration config = ResourceLoader.getConfig();
        stage = ConfigUtil.getString(NAME, config, "stage");
    }
}

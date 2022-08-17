package org.metamechanists.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

public class Log {

    static Logger logger;

    public static void initialize() {
        logger = PluginStorage.getPlugin().getLogger();
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void missingKeyError(String configName, ConfigurationSection section, String key) {
        warning(configName + "." + section.getCurrentPath() + " missing '" + key + "'");
    }
}

/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.metaapi.util.Log;



public class ConfigUtil {
    private ConfigUtil() {}

    public static @Nullable ConfigurationSection getConfigurationSection(String configName, ConfigurationSection section, String key) {
        ConfigurationSection value = section.getConfigurationSection(key);
        if (value == null) {
            Log.missingKeyError(configName, section, key);
            return null;
        }

        return value;
    }

    public static int getInt( ConfigurationSection section, String key) {
        // No null check because int cannot be null, and 0 could be a valid parameter
        return section.getInt(key);
    }

    public static @Nullable String getString(String configName, ConfigurationSection section, String key) {
        String value = section.getString(key);
        if (value == null) {
            Log.missingKeyError(configName, section, key);
            return null;
        }

        return value;
    }
}

/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.config;

import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.metamechanists.metaapi.util.PluginStorage;

import java.io.File;


public class ResourceLoader {

    private static final String LANGUAGE_PATH = "language.yml";
    @Getter
    private static FileConfiguration language;

    private static void saveDefaultResources() {
        PluginStorage.getPlugin().saveResource(LANGUAGE_PATH, true);
    }

    private static void loadResources() {
        final File dataFolder = PluginStorage.getPlugin().getDataFolder();
        language = YamlConfiguration.loadConfiguration(new File(dataFolder, LANGUAGE_PATH));
    }

    public static void initialize() {
        saveDefaultResources();
        loadResources();
    }
}

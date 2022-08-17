package org.metamechanists.config;

import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.metamechanists.util.PluginStorage;

import java.io.File;


public class ResourceLoader {

    private static final String LANGUAGE_PATH = "language.yml";
    private static File dataFolder;
    @Getter
    private static FileConfiguration config;
    @Getter
    private static FileConfiguration language;

    private static void saveDefaultResources() {
        // Save config.yml
        PluginStorage.getPlugin().saveDefaultConfig();
        PluginStorage.getPlugin().saveResource(LANGUAGE_PATH, true);
    }

    private static void loadResources() {
        dataFolder = PluginStorage.getPlugin().getDataFolder();
        config = PluginStorage.getPlugin().getConfig();
        language = YamlConfiguration.loadConfiguration(new File(dataFolder, LANGUAGE_PATH));
    }

    public static void initialize() {
        saveDefaultResources();
        loadResources();
    }
}

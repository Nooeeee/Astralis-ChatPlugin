package me.nooeee.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import me.nooeee.Main;

import java.io.File;

public class Config {
    private static final File DATA_FOLDER = Main.plugin.getDataFolder();
    public static FileConfiguration settings = Main.plugin.getConfig();

    /**
     * Initialise et charge la configuration
     */
    public static void initialize() {
        Main.plugin.saveDefaultConfig();
        reload();
    }

    /**
     * Recharge la configuration depuis config.yml
     */
    public static void reload() {
        Main.plugin.reloadConfig();
        settings = Main.plugin.getConfig();
    }
}

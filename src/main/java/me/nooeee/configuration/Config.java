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

        // Crée dossier si inexistant
        if (!DATA_FOLDER.exists()) {
            DATA_FOLDER.mkdirs();
        }
        // Load config.yml depuis resources si inexistant
        Main.plugin.saveDefaultConfig();
        reload();
    }

    // Recharge la configuration depuis config.yml
    public static void reload() {
        Main.plugin.reloadConfig();
        settings = Main.plugin.getConfig();
    }

    // Save le config.yml actuel
    public static void save() {
        try {
            Main.plugin.saveConfig();
        } catch (Exception e) {
            Main.plugin.getLogger().severe("Erreur lors de la sauvegarde de config.yml: " + e.getMessage());
        }
    }
}

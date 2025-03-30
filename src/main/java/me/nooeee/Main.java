package me.nooeee;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import me.nooeee.command.Command;
import  me.nooeee.command.TabComplete;
import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import  me.nooeee.listener.PlayerChat;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static JavaPlugin plugin = null;
    public static boolean placeHolderAPI = false;

    @Override
    public void onEnable() {
        plugin = this;

        // Configuration : Config.yml
        saveDefaultConfig();
        Config.initialize();

        // Listener
        getServer().getPluginManager().registerEvents(new PlayerChat(ChatHandler.getInstance()), this);

        // Commands
        Objects.requireNonNull(getCommand("chatplugin")).setExecutor(new Command(ChatHandler.getInstance()));
        Objects.requireNonNull(getCommand("chatplugin")).setTabCompleter(new TabComplete());

        if (!setupPlaceHolderAPI()) {
            getLogger().warning("PlaceHolderAPI not found, local chat will not work!");
        }

    }

    private boolean setupPlaceHolderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return false;
        }

        placeHolderAPI = true;
        return true;
    }
}
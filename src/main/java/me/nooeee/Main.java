package me.nooeee;

import me.nooeee.command.*;
import me.nooeee.handler.StaffChatPreferences;
import me.nooeee.listener.JoinLeaveListener;
import me.nooeee.listener.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import me.nooeee.listener.PlayerChatListener;

import java.util.Objects;

import static me.nooeee.configuration.StatusManager.loadStatus;

public class Main extends JavaPlugin {
    public static JavaPlugin plugin = null;
    public static boolean placeHolderAPI = false;
    public static boolean luckPermsAPI = false;
    public static LuckPerms luckPerms;

    @Override
    public void onEnable() {
        plugin = this;

        // Configuration : Config.yml
        saveDefaultConfig();
        Config.initialize();

        // Save Activation/Désactivation du StaffChat
        StaffChatPreferences.loadFromConfig();

        // [Statuts] Sauvegarde
        loadStatus();

        // Listener
        getServer().getPluginManager().registerEvents(new PlayerChatListener(ChatHandler.getInstance()), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        // Commands
            // Plugin : Config
        Objects.requireNonNull(getCommand("chatplugin")).setExecutor(new CommandReload(ChatHandler.getInstance()));
        Objects.requireNonNull(getCommand("disablechat")).setExecutor(new CommandDisabledChat(plugin));
        Objects.requireNonNull(getCommand("staffchat")).setExecutor(new CommandStaffChat());

            // Roleplay
        Objects.requireNonNull(getCommand("anonyme")).setExecutor(new CommandAnonyme(ChatHandler.getInstance()));
        Objects.requireNonNull(getCommand("me")).setExecutor(new CommandMe(ChatHandler.getInstance()));
        Objects.requireNonNull(getCommand("roll")).setExecutor(new CommandRoll());

        // [PAYANT] Statut
        Objects.requireNonNull(getCommand("statut")).setExecutor(new CommandStatut());

            // Annonces pôles
        Objects.requireNonNull(getCommand("annonce-animation")).setExecutor(new CommandAnnonce());
        Objects.requireNonNull(getCommand("annonce-moderation")).setExecutor(new CommandAnnonce());
        Objects.requireNonNull(getCommand("annonce-scenariste")).setExecutor(new CommandAnnonce());

            // Annonces globales
        Objects.requireNonNull(getCommand("broadcast")).setExecutor(new CommandBroadcast());
        Objects.requireNonNull(getCommand("narration")).setExecutor(new CommandNarration());

        // TabComplete
            // Plugin : Config
        Objects.requireNonNull(getCommand("chatplugin")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand(("disablechat"))).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand(("staffchat"))).setTabCompleter(new TabComplete());

            // Roleplay
        Objects.requireNonNull(getCommand("anonyme")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("me")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("roll")).setTabCompleter(new TabComplete());

            // [PAYANT] Statut
        Objects.requireNonNull(getCommand("statut")).setTabCompleter(new TabComplete());


            // Annonces pôles
        Objects.requireNonNull(getCommand("annonce-animation")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("annonce-moderation")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("annonce-scenariste")).setTabCompleter(new TabComplete());

            // Annonces globales
        Objects.requireNonNull(getCommand("broadcast")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("narration")).setTabCompleter(new TabComplete());


        // Vérification et activation des dépendances
        if (!setupPlaceHolderAPI()) {
            getLogger().warning("PlaceHolderAPI not found, roleplay chat will not work!");
        }
        if(!setupLuckPermsAPI()) {
            getLogger().warning("LuckPermsAPI not found, prefix in chat will not work!");
        }
    }

    @Override
    public void onDisable() {

        // Save Activation/Désactivation du StaffChat
        StaffChatPreferences.saveToConfig();
    }

    private boolean setupPlaceHolderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return false;
        }

        placeHolderAPI = true;
        return true;
    }

    private boolean setupLuckPermsAPI() {
        if(Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
            return false;
        }
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(provider != null) {
            luckPerms = provider.getProvider();
        }

        luckPermsAPI = (luckPerms != null);
        return luckPermsAPI;
    }

}
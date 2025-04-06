package me.nooeee.command;

import me.nooeee.Main;
import me.nooeee.configuration.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class CommandDisabledChat implements CommandExecutor {
    private static final String[] POSSIBLE_VALUES = {"enable", "disable"};
    private final Logger pluginLogger;

    public CommandDisabledChat(JavaPlugin plugin) {
        this.pluginLogger = plugin.getLogger(); // Initialisation via le plugin principal
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("disablechat.admin")) {
            if(args.length == 0) {
                boolean isEnabled = Config.settings.getBoolean("chat-settings.chat-disabled");
                sender.sendMessage(disableChat(isEnabled));
                return true;

            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase(POSSIBLE_VALUES[0])) {
                    // Enabled (false dans config)
                    Config.settings.set("chat-settings.chat-disabled", false);

                } else if (args[0].equalsIgnoreCase(POSSIBLE_VALUES[1])) {
                    // Disabled (true dans config)
                    Config.settings.set("chat-settings.chat-disabled", true);
                } else {
                    return false;
                }

                // Reload le Config.yml
                Main.plugin.saveConfig();
                Config.reload();

                boolean newState = !Config.settings.getBoolean("chat-settings.chat-disabled");
                sender.sendMessage(disableChat(newState));
                pluginLogger.info("Le chat HRP est désormais " + (newState ? "activé" : "désactivé"));

                return true;
            }
        }
        return false;
    }

    private Component disableChat(boolean isEnabled) {
        return Component.text()
                .content("Le chat HRP est ")
                .append(Component.text(
                        isEnabled ? "activé" : "désactivé",
                        isEnabled ? NamedTextColor.GREEN : NamedTextColor.RED
                ).decorate(TextDecoration.BOLD))
                .color(NamedTextColor.WHITE) // Couleur texte principal
                .build();
    }
}

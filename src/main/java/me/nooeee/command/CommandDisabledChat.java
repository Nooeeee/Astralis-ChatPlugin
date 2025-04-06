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
    private final Logger pluginLogger;

    public CommandDisabledChat(JavaPlugin plugin) {
        this.pluginLogger = plugin.getLogger(); // Initialisation via le plugin principal
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("disablechat.admin")) {
            if(args.length == 0) {

                boolean isEnabled = Config.settings.getBoolean("chat-settings.chat-disabled");
                Config.settings.set("chat-settings.chat-disabled", !isEnabled);

                // Reload le Config.yml
                Main.plugin.saveConfig();
                Config.reload();

                pluginLogger.info("Le chat HRP est désormais " + (!isEnabled ? "activé" : "désactivé"));
                sender.sendMessage(disableChat(!isEnabled));
                return true;

            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info")) {

                    boolean isEnabled = Config.settings.getBoolean("chat-settings.chat-disabled");
                    sender.sendMessage(disableChat(isEnabled));
                    return true;

                }
                return true;
            }

        }
        return false;
    }

    private Component disableChat(boolean isEnabled) {
        return Component.text()
                .content("Le chat HRP est ")
                .append(Component.text(
                        isEnabled ? "désactivé" : "activé",
                        isEnabled ? NamedTextColor.RED : NamedTextColor.GREEN
                ).decorate(TextDecoration.BOLD))
                .color(NamedTextColor.WHITE) // Couleur texte principal
                .build();
    }
}

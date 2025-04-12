package me.nooeee.command;

import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandAnonyme implements CommandExecutor {

    private final ChatHandler chatHandler;

    public CommandAnonyme(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("§cSeuls les joueurs peuvent utiliser cette commande", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage : /anonyme [radius] <action>");
            return true;
        }

        int radius = Config.settings.getInt("chat-settings.roleplay.radius", 10);
        String action;

        try {
            radius = Integer.parseInt(args[0]);
            action = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";

        } catch (NumberFormatException e) {

            action = String.join(" ",args);
        }
        if (radius > 50) {
            player.sendMessage(Component.text("La portée doit être inférieur à 50 mètres !", NamedTextColor.RED));
            return true;
        } else if (action.isEmpty()) {
            player.sendMessage(Component.text("Vous devez spécifier une action.", NamedTextColor.RED));
            return true;
        }

        ChatHandler.sendRolePlayAnonyme(player, action, radius);
        return true;
    }
}

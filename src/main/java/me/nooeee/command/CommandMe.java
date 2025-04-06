package me.nooeee.command;

import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandMe implements CommandExecutor {

    private final ChatHandler chatHandler;

    public CommandMe(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage : /me [radius] <action>");
            return true;
        }

        int radius = Config.settings.getInt("chat-settings.local.radius", 10);
        String action;

        try {
            radius = Integer.parseInt(args[0]);
            action = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";
        } catch (NumberFormatException e)  {
            // Radius de 10 par défaut ; si pas d'int en 1er arg, met tout dans l'action
            action = String.join(" ", args);
        }

        if(action.isEmpty()) {
            player.sendMessage("§cVous devez spécifier une action");
            return true;
        }

        chatHandler.sendRolePlayMe(player, action, radius);
        return true;
    }
}

package me.nooeee.command;

import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandReload implements CommandExecutor {

    private final ChatHandler chatHandler;

    public CommandReload(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!sender.hasPermission("chatplugin.reload")) {
            sender.sendMessage(ChatHandler.formatMessage(Config.settings.getString("messages.error.permission")));
            return true;
        }

        if(args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatHandler.formatMessage(Config.settings.getString("messages.error.args")));
            return true;
        }

        Config.reload();
        sender.sendMessage(ChatHandler.formatMessage(Config.settings.getString("messages.reloaded")));

        return true;
    }
}

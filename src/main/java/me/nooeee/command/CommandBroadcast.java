package me.nooeee.command;

import me.nooeee.configuration.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandBroadcast implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof ConsoleCommandSender || (sender.isOp() || sender.hasPermission("annonce.broadcast"))) {

            if(args.length == 0) {
                sender.sendMessage(Component.text("Veuillez préciser un message.", NamedTextColor.RED));
                return true;
            }

            String message = String.join(" ", args);
            String format = Config.settings.getString("chat-settings.annonce.broadcast", "%message%");

            String formattedMessage = format.replace("%message%", message);

            Component finalMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedMessage);
            Bukkit.broadcast(finalMessage);
        }
        return false;
    }
}

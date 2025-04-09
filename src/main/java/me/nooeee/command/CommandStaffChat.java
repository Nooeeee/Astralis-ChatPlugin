package me.nooeee.command;

import me.nooeee.configuration.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.nooeee.handler.ChatHandler.sendStaffMessage;
import static me.nooeee.handler.StaffChatPreferences.*;

public class CommandStaffChat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Seuls les joueurs peuvent utiliser cette commande", NamedTextColor.RED));
            return true;
        }

        if (!player.hasPermission("astralis.staff")) return true;

        if (args.length == 0) {
            toggleStaffChat(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("enabled") || args[0].equalsIgnoreCase("disabled")
                || args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {

            boolean desiredState = args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enabled");

            if (desiredState == staffChatIsEnabled(player)) {
                String message = desiredState
                        ? "§6Ton StaffChat est déjà §a§lactivé"
                        : "§6Ton StaffChat est déjà §c§ldésactivé";
                player.sendMessage(message);
                return true;
            }

            notifyStaffStatusChange(player, desiredState);

            if (desiredState) {
                enabledPlayers.add(player.getUniqueId());
            } else {
                enabledPlayers.remove(player.getUniqueId());
            }

            String message = desiredState
                    ? Config.settings.getString("chat-settings.staff-chat.staffChatEnabled")
                    : Config.settings.getString("chat-settings.staff-chat.staffChatDisabled");

            sendFormattedMessage(player, message);

            return true;
        }

        if (staffChatIsEnabled(player)) {
            String message = String.join(" ", args);
            sendStaffMessage(player, message);
        } else {
            player.sendMessage("§cVotre Staff chat n'est pas activé !");
            return true;
        }


        return true;
    }

}

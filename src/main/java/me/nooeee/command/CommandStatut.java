package me.nooeee.command;

import me.nooeee.configuration.Config;
import me.nooeee.configuration.StatusManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandStatut implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0 && (sender.hasPermission("statut") || sender.isOp())) {
            sender.sendMessage(Component.text("Usage : /statut <message/info>", NamedTextColor.RED));
            return true;
        }

        // Commande de base: /statut <message>
        if (args.length > 0 && !args[0].equalsIgnoreCase("info")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande");
                return true;
            }

            if (!player.hasPermission("statut") || !player.isOp()) {
                player.sendMessage(Config.settings.getString("messages.error.permission"));
                return true;
            }

            String inputText = String.join(" ", args);
            if (inputText.contains("&")) {
                player.sendMessage(Component.text("Les codes couleurs (&) ne sont pas autorisés dans les statuts.", NamedTextColor.RED));
                return true;
            }

            Component newStatus = LegacyComponentSerializer.legacySection().deserialize(inputText);
            StatusManager.saveStatus(player.getUniqueId(), newStatus);
            player.sendMessage(Component.text("Statut mis à jour : ", NamedTextColor.GREEN).append(newStatus));
            return true;
        }

        // Commande info: /statut info [joueur]
        if (args.length > 0 && args[0].equalsIgnoreCase("info")) {
            UUID targetId;

            // Si pas de joueur spécifié, vérifier son propre statut
            if (args.length == 1) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Usage: /statut info");
                    return true;
                }
                targetId = player.getUniqueId();
            }
            // Si joueur spécifié
            else {
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer != null) {
                    targetId = targetPlayer.getUniqueId();
                } else {
                    // Vérifier pour un joueur déconnecté
                    targetId = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                    if (!StatusManager.hasStatus(targetId)) {
                        sender.sendMessage(Component.text("Ce joueur n'existe pas ou n'a pas de statut !", NamedTextColor.RED));
                        return true;
                    }
                }
            }

            // Afficher le statut
            Component status = StatusManager.getStatus(targetId);
            String playerName = Bukkit.getOfflinePlayer(targetId).getName();

            if (status.equals(Component.empty())) {
                sender.sendMessage(Component.text(playerName + " n'a pas de statut défini.", NamedTextColor.GOLD));
            } else {
                sender.sendMessage(Component.text("Statut de " + playerName + " : ", NamedTextColor.YELLOW)
                        .append(status));
            }
            return true;
        }

        return false;
    }
}
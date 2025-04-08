package me.nooeee.command;

import me.nooeee.configuration.StatusManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class CommandStatut implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Commande de base (/statut <message>)
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Usage: /statut <message> ou /statut info <joueur>", NamedTextColor.RED));
                return true;
            }
            return setOwnStatus((Player) sender, args);
        }

        // Sous-commande info (/statut info [joueur])
        if (args[0].equalsIgnoreCase("info")) {
            return handleInfoCommand(sender, args);
        }

        return false;
    }

    private boolean setOwnStatus(Player player, String[] args) {
        if (!player.hasPermission("statut")) {
            player.sendMessage(Component.text("Permission manquante !", NamedTextColor.RED));
            return true;
        }

        Component newStatus = LegacyComponentSerializer.legacySection().deserialize(String.join(" ", args));
        StatusManager.saveStatus(player.getUniqueId(), newStatus);
        player.sendMessage(Component.text("Statut mis à jour : ", NamedTextColor.GREEN)
                .append(newStatus));
        return true;
    }

    private boolean handleInfoCommand(CommandSender sender, String[] args) {
        // Vérification permission
        if (!sender.hasPermission("statut.info") && !sender.isOp()) {
            sender.sendMessage(Component.text("Permission manquante !", NamedTextColor.RED));
            return true;
        }

        // Cas 1: /statut info (soi-même)
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Usage: /statut info <joueur>", NamedTextColor.RED));
                return true;
            }
            return showStatus(sender, ((Player) sender).getUniqueId());
        }

        // Cas 2: /statut info <joueur>
        return handlePlayerLookup(sender, args[1]);
    }

    private boolean handlePlayerLookup(CommandSender sender, String playerName) {
        // 1. Trouver le joueur (connecté ou non)
        Optional<UUID> playerId = findPlayerId(playerName);

        if (!playerId.isPresent()) {
            sender.sendMessage(Component.text("Joueur introuvable !", NamedTextColor.RED));
            return true;
        }

        // 2. Vérifier si le joueur a un statut enregistré
        if (!StatusManager.hasStatus(playerId.get())) {
            sender.sendMessage(Component.text("Ce joueur n'a jamais défini de statut.", NamedTextColor.GOLD));
            return true;
        }

        return showStatus(sender, playerId.get());
    }

    private boolean showStatus(CommandSender sender, UUID playerId) {
        Optional<Component> statusOpt = StatusManager.getPlayerStatus(playerId);

        statusOpt.ifPresentOrElse(
                status -> sender.sendMessage(buildStatusMessage(playerId, status)),
                () -> sender.sendMessage(Component.text("Erreur: Données corrompues pour ce joueur.", NamedTextColor.RED))
        );
        return true;
    }

    private Component buildStatusMessage(UUID playerId, Component status) {
        String playerName = Bukkit.getOfflinePlayer(playerId).getName() ?? "Inconnu";

        return Component.text()
                .content("Statut de " + playerName + ": ")
                .color(NamedTextColor.YELLOW)
                .append(status.equals(Component.empty()) ?
                        Component.text("(vide)", NamedTextColor.GRAY) :
                        status)
                .build();
    }

    private Optional<UUID> findPlayerId(String playerName) {
        // 1. Vérifier les joueurs connectés
        Player onlinePlayer = Bukkit.getPlayer(playerName);
        if (onlinePlayer != null) return Optional.of(onlinePlayer.getUniqueId());

        // 2. Vérifier les joueurs déconnectés (via le cache de Bukkit)
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (offlinePlayer.hasPlayedBefore()) {
            return Optional.of(offlinePlayer.getUniqueId());
        }

        return Optional.empty();
    }
}
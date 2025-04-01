package me.nooeee.command;

import me.nooeee.handler.ChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class CommandAnnonce implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur ou avec un radius spécifié.");
            return true;
        }

        Player player = (Player) sender;

        // Trouver le pôle selon la commande (annnonceanim -> Pole.ANIM)
        Pole pole = Pole.fromCommand(command.getName());
        if(pole == null) {
            sender.sendMessage(Component.text("Commande invalide.", NamedTextColor.RED));
            return true;
        }

        if(!player.hasPermission(pole.permission) && !player.isOp()) {
            sender.sendMessage(Component.text("Vous n'avez pas la permission d'effectuer cette commande.", NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /annonce" + pole.name().toLowerCase() + " <radius> <message>", NamedTextColor.RED));
            return true;
        }

        try {
            int radius = Integer.parseInt(args[0]);
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            String formattedMessage = pole.prefix + " : §f" + message;

            ChatHandler.getInstance().sendAnnonce(player, formattedMessage, radius);

        } catch(NumberFormatException e) {
            sender.sendMessage(Component.text("Le radius doit être un nombre.", NamedTextColor.RED));
        }

        return true;
    }
}

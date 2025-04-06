package me.nooeee.command;

import me.nooeee.handler.ChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class CommandRoll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande");
            return true;
        }

        Random random = new Random();
        int roll = random.nextInt(101);
        ChatHandler.sendRolePlayRoll(player, rollColor(roll), 10);
        return true;
    }

    private Component rollColor(int roll) {
        return Component.text()
                .content("a roll ")
                .color(NamedTextColor.WHITE)
                .append(
                        Component.text(roll)
                                .color(roll < 50 ? NamedTextColor.RED : NamedTextColor.GREEN)
                                .decorate(TextDecoration.BOLD)
                )
                .build();
    }
}

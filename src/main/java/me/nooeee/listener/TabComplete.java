package me.nooeee.listener;

import me.nooeee.command.Pole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        switch(label.toLowerCase()) {
            // Plugin : Config
            case "chatplugin":
                if(args.length == 1 && sender.hasPermission("chatplugin.reload")) {
                    suggestions.add("reload");
                }
                break;

            case "disablechat":
                if(sender.hasPermission("disablechat.admin") && args.length == 1) {
                    suggestions.add("info");
                }
                break;

            case "staffchat":
            case "sc":
                if(sender.hasPermission("astralis.staff") && args.length == 1) {
                    suggestions.addAll(List.of("enabled", "disabled", "message", "on", "off"));
                }
                break;

            // Roleplay
            case "me":
            case "anonyme":
                switch(args.length) {
                    case 1: suggestions.add("10"); break;
                    case 2: suggestions.add("action"); break;
                }
                break;

            case "roll":
                return Collections.emptyList();

            // [PAYANT] Statut
            case "statut":
                if(args.length == 1) {
                    suggestions.add("info");
                }
                break;

            // Annonces pôles
            case "annonce-animation":
            case "annonce-moderation":
            case "annonce-scenariste":
                Pole pole = Pole.fromCommand(command.getName());
                if(pole != null && (sender.isOp() || sender.hasPermission(pole.getPermission()))) {
                    switch(args.length) {
                        case 1: suggestions.add("50"); break;
                        case 2: suggestions.add("Message"); break;
                    }
                }
                break;

            // Annonces globales
            case "broadcast":
                if((sender.isOp() || sender.hasPermission("annonce.broadcast"))) {
                    if(args.length == 1) suggestions.add("Message");
                }
                break;

            case "narration":
                if((sender.isOp() || sender.hasPermission("annonce.narration"))) {
                    if(args.length == 1) suggestions.add("Message");
                }
                break;

        }

        return suggestions;
    }
}
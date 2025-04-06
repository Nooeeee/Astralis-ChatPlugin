package me.nooeee.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> suggestions = new ArrayList<>();

        if(args.length == 1 && command.getName().equalsIgnoreCase("chatplugin")) {
            if(sender.isOp() || sender.hasPermission("chatplugin.reload")) {
                suggestions.add("reload");
            }
        }

        else if(label.startsWith("annonce")) {
            Pole pole = Pole.fromCommand(command.getName());

            if(pole != null && (sender.isOp() || sender.hasPermission(pole.getPermission()))) {
                switch(args.length) {
                    case 1 -> suggestions.add("50");
                    case 2 -> suggestions.add("Message");
                    case 3 -> List.of("");
                }
            }
        }

        else if(label.startsWith("broadcast")) {
            if(sender.isOp() || sender.hasPermission("annonce.broadcast")) {
                switch(args.length) {
                    case 1 -> suggestions.add("Message");
                    case 2 -> List.of("");
                }
            }
        }

        else if(label.startsWith("narration")) {
            if(sender.isOp() || sender.hasPermission("annonce.narration")) {
                switch(args.length) {
                    case 1 -> suggestions.add("Message");
                    case 2 -> List.of("");
                }
            }
        }

        else if(label.startsWith("disablechat")) {
            if(sender.hasPermission("disablechat.admin")) {
                if(args.length == 1) {
                    return Arrays.asList("enable", "disable");
                }

                return Collections.emptyList();
            }
        }

        return suggestions;
    }
}

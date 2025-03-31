package me.nooeee.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.nooeee.command.Pole.fromCommand;

public class TabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> suggestions = new ArrayList<>();

        if(args.length == 1 && command.getName().equalsIgnoreCase("chatplugin")) {
            if(sender.isOp() || sender.hasPermission("chatplugin.reload")) {
                suggestions.add("reload");
            }

            return suggestions;
        }

        if(label.startsWith("annonce")) {
            Pole pole = Pole.fromCommand(command.getName());

            if(pole != null && (sender.isOp() || sender.hasPermission(pole.getPermission()))) {
                switch(args.length) {
                    case 1 -> suggestions.add("50");
                    case 2 -> suggestions.add("Message");
                    case 3 -> List.of("");
                }
            }
            return suggestions;
        }
        return suggestions;
    }
}

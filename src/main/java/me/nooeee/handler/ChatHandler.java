package me.nooeee.handler;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.nooeee.Main;
import me.nooeee.configuration.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.nooeee.handler.MessageType.*;

public class ChatHandler {
    private static ChatHandler instance;
    // Permet convertir "&" Minecraft --> component + couleur
    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    // private : bloque création via new ChatHandler()...
    private ChatHandler() {
    }

    public static ChatHandler getInstance() {
        if(instance == null) {
            instance = new ChatHandler();
        }
        return instance;
    }

    // Envoyer les messages dans le chat Global : HRP
    public static void writeToGlobalChat(AsyncChatEvent event, Player player, String message) {
        int radius = Config.settings.getInt("chat-settings.global.radius", 400);
        sendChatMessage(event, player, message, GLOBAL, radius);
    }

    // Envoyer les messages dans le chat Local : RP
    public static void writeToLocalChat(AsyncChatEvent event, Player player, String message) {
        int radius = Config.settings.getInt("chat-settings.local.radius", 10);
        sendChatMessage(event, player, message, LOCAL, radius);
    }

    // Envoyer dans le Chat Staff
    public static void sendStaffMessage(Player sender, String message) {
        String formatted = Config.settings.getString("chat-settings.staff-chat.staffChatFormat")
                .replace("%prefix%", getPlayerPrefix(sender))
                .replace("%player%", getPlayerDisplay(sender))
                .replace("%message%", message);

        if (Main.placeHolderAPI) {
            formatted = PlaceholderAPI.setPlaceholders(sender, formatted);
        }

        String finalFormatted = formatted;
        Bukkit.getOnlinePlayers().stream()
                .filter(recipient -> recipient.hasPermission("astralis.staff") && StaffChatPreferences.staffChatIsEnabled(recipient))
                .forEach(recipient -> recipient.sendMessage(LEGACY.deserialize(finalFormatted)));

        Bukkit.getConsoleSender().sendMessage(LEGACY.deserialize(finalFormatted));
    }

    // Envoi du message à tous les joueurs à proximité
    private static void sendChatMessage(AsyncChatEvent event, Player player, String message, MessageType type, int radius) {
        event.viewers().forEach(recipient -> {
            if(isPlayerHearChat(player, recipient, radius)) {
                recipient.sendMessage(formatMessage(type, player, message));
            }
        });
    }

    // Pour la commande : /me
    public static void sendRolePlayMe(Player sender, String message, int radius) {
        String formatted = Config.settings.getString("chat-settings.local.format", "%prefix% %player% %message%")
                .replace("%prefix%", getPlayerPrefix(sender))
                .replace("%player%", getPlayerDisplay(sender))
                .replace("%message%", message);

        if (Main.placeHolderAPI) {
            formatted = PlaceholderAPI.setPlaceholders(sender, formatted);
        }

        TextComponent component = LEGACY.deserialize(formatted);

        for (Player recipient : sender.getWorld().getPlayers()) {
            if(isPlayerHearChat(sender,recipient, radius)) {
                recipient.sendMessage(component);
            }
        }
    }

    // Pour la commande : /roll
    public static void sendRolePlayRoll(Player sender, Component message, int radius) {
        String formatted = "%prefix% %player% " + LegacyComponentSerializer.legacySection().serialize(message);
        formatted = formatted
                .replace("%prefix%", getPlayerPrefix(sender))
                .replace("%player%", getPlayerDisplay(sender));

        if (Main.placeHolderAPI) {
            formatted = PlaceholderAPI.setPlaceholders(sender, formatted);
        }

        TextComponent component = LEGACY.deserialize(formatted);

        for (Player recipient : sender.getWorld().getPlayers()) {
            if(isPlayerHearChat(sender,recipient, radius)) {
                recipient.sendMessage(component);
            }
        }
    }

    // Pour la commande : /annonce-pole (ex /annonce-animation
    public static void sendAnnonce(Player sender, String message, int radius) {
        TextComponent formattedMessage = formatMessage(message);
        for(Player recipient : sender.getWorld().getPlayers()) {
            if(isPlayerHearChat(sender, recipient, radius)) {
                recipient.sendMessage(formattedMessage);
            }
        }
    }

    // Boolean pour récup les joueurs à proximités
    private static boolean isPlayerHearChat(Player sender, Audience recipient, int radius) {
        if(recipient instanceof ConsoleCommandSender) {
            return true;
        }

        if(radius > 0 && recipient instanceof Player receiver) {
            return sender.getWorld().equals(receiver.getWorld()) &&
                    sender.getLocation().distanceSquared(receiver.getLocation()) <= (radius * radius);
        }

        return radius <= 0;
    }


    // Formatter le message via son type (global, local) + prefix et displayName
    public static TextComponent formatMessage(MessageType type, Player player, String message) {
        String formatted = switch (type) {
            case GLOBAL -> Config.settings.getString("chat-settings.global.format", "%prefix% %player%: %message%");
            case LOCAL -> Config.settings.getString("chat-settings.local.format", "%prefix% %player%: %message%");
            case OTHER -> message;
        };

        if(type == LOCAL && message != null && message.startsWith("*")) {
            message = message.substring(1).trim();
        }

        String result = formatted
                .replace("%prefix%", getPlayerPrefix(player))
                .replace("%player%", getPlayerDisplay(player))
                .replace("%message%", message != null ? message : "");

        if (Main.placeHolderAPI) {
            result = PlaceholderAPI.setPlaceholders(player, result);
        }

        return LEGACY.deserialize(result);
    }

    public static String getPlayerDisplay(Player player) {
        return LEGACY.serialize(player.displayName());
    }

    public static String getPlayerPrefix(Player player) {

        // Convertir le prefix en string
        String prefix = "";

        if(Main.luckPermsAPI && Main.luckPerms != null) {
            User user = Main.luckPerms.getUserManager().getUser(player.getUniqueId());
            if(user != null) {
                prefix = user.getCachedData().getMetaData().getPrefix();
                if(prefix == null) {
                    prefix = "";
                }
            }
        }

        return prefix;
    }

    public static TextComponent formatMessage(String message) {
        return LEGACY.deserialize(message);
    }

}

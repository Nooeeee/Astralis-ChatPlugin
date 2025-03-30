package me.nooeee.handler;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.nooeee.Main;
import me.nooeee.configuration.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.nooeee.handler.MessageType.*;

public class ChatHandler {
    private static ChatHandler instance;
    // Permet convertir "&" Minecraft --> component + couleur
    private final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    // private : bloque création via new ChatHandler()...
    private ChatHandler() {
    }

    public static ChatHandler getInstance() {
        if(instance == null) {
            instance = new ChatHandler();
        }
        return instance;
    }

    // Global : HRP
    public void writeToGlobalChat(AsyncChatEvent event, Player player, String message) {
        int radius = Config.settings.getInt("chat-settings.global.radius", 400);
        sendChatMessage(event, player, message, GLOBAL, radius);
    }

    // Local : RP
    public void writeToLocalChat(AsyncChatEvent event, Player player, String message) {
        int radius = Config.settings.getInt("chat-settings.local.radius", 10);
        sendChatMessage(event, player, message, LOCAL, radius);
    }

    private void sendChatMessage(AsyncChatEvent event, Player player, String message, MessageType type, int radius) {
        event.viewers().forEach(recipient -> {
            if(isPlayerHearChat(player, recipient, radius)) {
                recipient.sendMessage(formatMessage(type, player, message));
            }
        });
    }

    private boolean isPlayerHearChat(Player sender, Audience recipient, int radius) {
        if(recipient instanceof ConsoleCommandSender) {
            return true;
        }

        if(radius > 0 && recipient instanceof Player receiver) {
            return sender.getWorld().equals(receiver.getWorld()) &&
                    sender.getLocation().distanceSquared(receiver.getLocation()) <= (radius * radius);
        }

        return radius <= 0;
    }

    public TextComponent formatMessage(MessageType type, Player player, String message) {
        String formatted = switch (type) {
            case GLOBAL -> Config.settings.getString("chat-settings.global.format", "%player%: %message%");
            case LOCAL -> Config.settings.getString("chat-settings.local.format", "%player%: %message%");
            case OTHER -> message;
        };

        String result = formatted.replace("%player%", player.getName())
                .replace("%message%", message != null ? message : "");

        if (Main.placeHolderAPI) {
            result = PlaceholderAPI.setPlaceholders(player, result);
        }

        return LEGACY.deserialize(result);
    }

    public TextComponent formatMessage(String message) {
        return LEGACY.deserialize(message);
    }

}

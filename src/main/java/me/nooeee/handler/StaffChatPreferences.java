package me.nooeee.handler;

import me.nooeee.configuration.Config;
import me.nooeee.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static me.clip.placeholderapi.PlaceholderAPI.setPlaceholders;
import static me.nooeee.handler.ChatHandler.getPlayerDisplay;
import static me.nooeee.handler.ChatHandler.getPlayerPrefix;

public class StaffChatPreferences {

    // Si UUID est dans le HashSet -> Staffchat ON
    public static final Set<UUID> enabledPlayers = new HashSet<>();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    public static void toggleStaffChat(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isNowEnabled = enabledPlayers.contains(uuid);

        if (isNowEnabled) {
            // Désactive Staff Chat
            enabledPlayers.remove(uuid);
            sendFormattedMessage(player, Config.settings.getString("chat-settings.staff-chat.staffChatDisabled"));
        } else {
            // Active Staff Chat
            enabledPlayers.add(uuid);
            sendFormattedMessage(player, Config.settings.getString("chat-settings.staff-chat.staffChatEnabled"));
        }

        notifyStaffStatusChange(player, !isNowEnabled);
    }

    public static boolean staffChatIsEnabled(Player player) {
        return enabledPlayers.contains(player.getUniqueId());
    }

    public static void notifyStaffStatusChange(Player player, boolean isNowEnabled) {
        String configPath = isNowEnabled
                ? "chat-settings.staff-chat.staffChatPlayerEnabled"
                : "chat-settings.staff-chat.staffChatPlayerDisabled";

        String message = formatMessage(player, Config.settings.getString(configPath));
        Bukkit.getOnlinePlayers().stream()
                .filter(recipient -> recipient.hasPermission("astralis.staff"))
                .forEach(recipient -> recipient.sendMessage(LEGACY.deserialize(message)));

        Bukkit.getConsoleSender().sendMessage(LEGACY.deserialize(message));
    }

    public static void sendFormattedMessage(Player player, String message) {
        player.sendMessage(LEGACY.deserialize(formatMessage(player, message)));
    }

    private static String formatMessage(Player player, String text) {
        String formatted = text
                .replace("%prefix%", getPlayerPrefix(player))
                .replace("%player%", getPlayerDisplay(player));

        if (Main.placeHolderAPI) {
            formatted = setPlaceholders(player, formatted);
        }

        return formatted;
    }

    public static void saveToConfig() {
        Config.settings.set("staff-chat.disabled-players", enabledPlayers.stream().map(UUID::toString).toList());
        Config.save();
    }

    public static void loadFromConfig() {
        enabledPlayers.clear();
        Config.settings.getStringList("staff-chat.disabled-players")
                .forEach(uuid -> enabledPlayers.add(UUID.fromString(uuid)));
    }
}
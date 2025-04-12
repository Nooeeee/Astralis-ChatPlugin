package me.nooeee.listener;

import me.nooeee.configuration.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.nooeee.configuration.StatusManager.*;
import static me.nooeee.handler.ChatHandler.LEGACY;

public class JoinLeaveListener implements Listener {

    private static final String JOIN_MESSAGE_PATH = "messages.join";
    private static final String QUIT_MESSAGE_PATH = "messages.leave";
    private static final String STATUS_JOIN_PATH = "messages.statut-format";

    private static final String DEFAULT_JOIN_MESSAGE = "&a♣ &r%player% a rejoint Clover ! ✔";
    private static final String DEFAULT_QUIT_MESSAGE = "&c♣ &r%player% est parti de Clover ! ✘";
    private static final String STATUS_DEFAULT_JOIN_MESSAGE = "&7[&a+&7] &8%player% : &9%status%";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Config.settings.getBoolean("join-leave", true)) {
            event.joinMessage(null);
            return;
        }

        Player player = event.getPlayer();
        String finalMessage;

        if (player.hasPermission("statut") || player.isOp()) {
            if (hasStatus(player.getUniqueId())) {
                String statusFormat = Config.settings.getString(STATUS_JOIN_PATH, STATUS_DEFAULT_JOIN_MESSAGE);
                Component playerStatus = getStatus(player.getUniqueId());

                finalMessage = statusFormat
                        .replace("%player%", player.getName())
                        .replace("%status%", LEGACY.serialize(playerStatus));

                event.joinMessage(LEGACY.deserialize(finalMessage));
                return;
            }
        }

        String sysMessage = Config.settings.getString(JOIN_MESSAGE_PATH, DEFAULT_JOIN_MESSAGE);

        finalMessage = sysMessage.replace("%player%", player.getName());
        event.joinMessage(LEGACY.deserialize(finalMessage));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!Config.settings.getBoolean("join-leave", true)) {
            event.quitMessage(null);
            return;
        }

        Player player = event.getPlayer();

        String sysMessage = Config.settings.getString(QUIT_MESSAGE_PATH, DEFAULT_QUIT_MESSAGE);

        String formatted = sysMessage.replace("%player%", player.getName());
        event.quitMessage(LEGACY.deserialize(formatted));
    }
}
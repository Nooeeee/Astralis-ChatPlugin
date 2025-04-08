package me.nooeee.listener;

import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.nooeee.handler.ChatHandler.LEGACY;

public class JoinLeaveListener implements Listener {

    private static final String JOIN_MESSAGE_PATH = "messages.join";
    private static final String QUIT_MESSAGE_PATH = "messages.leave";

    private static final String DEFAULT_JOIN_MESSAGE = "&a♣ &r%player% a rejoint Clover ! ✔";
    private static final String DEFAULT_QUIT_MESSAGE = "&c♣ &r%player% est parti de Clover ! ✘";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Config.settings.getBoolean("join-leave", true)) {
            event.joinMessage(null);
            return;
        }

        Player player = event.getPlayer();

        String sysMessage = Config.settings.getString(JOIN_MESSAGE_PATH, DEFAULT_JOIN_MESSAGE);

        String formatted = sysMessage.replace("%player%", ChatHandler.getPlayerDisplay(player));
        event.joinMessage(LEGACY.deserialize(formatted));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!Config.settings.getBoolean("join-leave", true)) {
            event.quitMessage(null);
            return;
        }

        Player player = event.getPlayer();

        String sysMessage = Config.settings.getString(QUIT_MESSAGE_PATH, DEFAULT_QUIT_MESSAGE);

        String formatted = sysMessage.replace("%player%", ChatHandler.getPlayerDisplay(player));
        event.quitMessage(LEGACY.deserialize(formatted));
    }
}
package me.nooeee.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChat implements Listener {
    private final ChatHandler chatHandler;

    public PlayerChat(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        TextComponent message = (TextComponent) event.message();
        String contentMessage = message.content().trim();

        if (contentMessage.startsWith("*")) {
            if (contentMessage.replace("*", "").trim().isEmpty()) {
                return;
            } else {
                canWriteToLocalChat(event, player, message);
            }
        } else if ((!(player.hasPermission("disablechat.admin") || player.hasPermission("disablechat.bypass")) || player.isOp()) && (Config.settings.getBoolean("chat-settings.chat-disabled"))) {
            player.sendMessage("§cLe chat HRP est actuellement §lDésactivé");
            return;
        } else {
            canWriteToGlobalChat(event, player, message);
        }
    }

    private void canWriteToGlobalChat(AsyncChatEvent event, Player player, TextComponent message) {
        if (!player.hasPermission("chatplugin.global") && !player.isOp()) {
            player.sendMessage(chatHandler.formatMessage(Config.settings.getString("messages.error.permission")));
            return;
        }

        chatHandler.writeToGlobalChat(event, player, message.content());
    }

    private void canWriteToLocalChat(AsyncChatEvent event, Player player, TextComponent message) {
        if (!player.hasPermission("chatplugin.local") && !player.isOp()) {
            player.sendMessage(chatHandler.formatMessage(Config.settings.getString("messages.error.permission")));
            return;
        }

        chatHandler.writeToLocalChat(event, player, message.content());
    }
}

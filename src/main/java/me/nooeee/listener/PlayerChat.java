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
        event.setCancelled(true);
        Player player = event.getPlayer();
        TextComponent message = (TextComponent) event.message();

        if(message.content().startsWith("*")) {
            writeToTheLocalChat(event, player, message);
        } else {
            writeToTheGlobalChat(event, player, message);
        }
    }

    private void writeToTheGlobalChat(AsyncChatEvent event, Player player, TextComponent message) {
        if(!player.hasPermission("chatplugin.global") && !player.isOp()) {
            player.sendMessage(chatHandler.formatMessage(Config.settings.getString("messages.error.permission")));
            return;
        }

        chatHandler.writeToGlobalChat(event, player, message.content());
    }

    private void writeToTheLocalChat(AsyncChatEvent event, Player player, TextComponent message) {
        if(!player.hasPermission("chatplugin.local") && !player.isOp()) {
            player.sendMessage(chatHandler.formatMessage(Config.settings.getString("messages.error.permission")));
            return;
        }

        chatHandler.writeToLocalChat(event, player, message.content());
    }
}

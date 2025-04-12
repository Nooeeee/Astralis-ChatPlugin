package me.nooeee.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.nooeee.configuration.Config;
import me.nooeee.handler.ChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {
    private final ChatHandler chatHandler;

    public PlayerChatListener(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        TextComponent message = (TextComponent) event.message();

        if (message.contains(Component.text("&"))) {
            player.sendMessage(Component.text("Les codes couleurs (&) ne sont pas autorisés dans les messages.", NamedTextColor.RED));
            return;
        }

        String contentMessage = message.content().trim();

        if (contentMessage.startsWith("*")) {
            if (contentMessage.replace("*", "").trim().isEmpty()) {
                return;
            } else {
                canWriteToLocalChat(event, player, message);
            }
        } else if ((!(player.hasPermission("disablechat.admin")
                || player.hasPermission("disablechat.bypass"))
                || player.isOp())
                && (Config.settings.getBoolean("chat-settings.chat-disabled"))) {

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
        if (!player.hasPermission("chatplugin.roleplay") && !player.isOp()) {
            player.sendMessage(chatHandler.formatMessage(Config.settings.getString("messages.error.permission")));
            return;
        }

        chatHandler.writeToRoleplayChat(event, player, message.content());
    }
}

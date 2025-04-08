package me.nooeee.configuration;

import me.nooeee.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class StatusManager {
    private static final File STATUS_FILE = new File(Main.plugin.getDataFolder(), "status.yml");
    private static YamlConfiguration statusConfig;
    private static final ConcurrentHashMap<UUID, Component> statusCache = new ConcurrentHashMap<>();

    public static void loadStatus() {
        Main.plugin.getServer().getAsyncScheduler().runNow(Main.plugin, task -> {
            // Crée le fichier si inexistant
            if (!STATUS_FILE.exists()) {
                try {
                    STATUS_FILE.createNewFile();
                } catch (IOException e) {
                    Main.plugin.getLogger().severe("Erreur lors de la création du status.yml: " + e.getMessage());
                    return;
                }
            }

            // Charger la config
            statusConfig = YamlConfiguration.loadConfiguration(STATUS_FILE);

            // Remplir le cache
            statusConfig.getConfigurationSection("players").getKeys(false).forEach(uuidStr -> {
                UUID uuid = UUID.fromString(uuidStr);
                String legacyText = statusConfig.getString("players." + uuidStr);
                statusCache.put(uuid, LegacyComponentSerializer.legacySection().deserialize(legacyText));
            });

            Main.plugin.getLogger().info(statusCache.size() + " status chargés (Astralis-ChatPlugin)");
        });
    }

    public static void saveStatus(UUID playerId, Component status) {
        // Màj du cache
        statusCache.put(playerId, status);

        // Save
        Main.plugin.getServer().getAsyncScheduler().runDelayed(Main.plugin, task -> {
            String legacyText = LegacyComponentSerializer.legacySection().serialize(status);
            statusConfig.set("players." + playerId, legacyText);

            try {
                statusConfig.save(STATUS_FILE);
            } catch (IOException e) {
                Main.plugin.getLogger().severe("Erreur lors de la sauvegarde status: " + e.getMessage());
            }
        }, 1, TimeUnit.SECONDS); // Délai optionnel
    }

    public static Component getStatus(UUID playerId) {
        return statusCache.getOrDefault(playerId, Component.empty());
    }

    /**
     * Vérifie si un joueur a un statut enregistré (même s'il est déco)
     * @return true si le joueur existe dans le fichier, false sinon
     */
    public static boolean hasStatus(UUID playerId) {
        return statusCache.containsKey(playerId) ||
                (statusConfig != null && statusConfig.contains("players." + playerId));
    }

    /**
     * Recup le statut d'un joueur (deconnecté compris)
     * @return Optional<Component> vide si le joueur n'existe pas, ou Optional avec le statut (peut être empty())
     */
    public static Optional<Component> getPlayerStatus(UUID playerId) {
        if (!hasStatus(playerId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(statusCache.get(playerId))
                .or(() -> {
                    // Charger depuis le fichier si absent du cache
                    String legacyText = statusConfig.getString("players." + playerId);
                    return Optional.of(legacyText != null ?
                            LegacyComponentSerializer.legacySection().deserialize(legacyText) :
                            Component.empty());
                });
    }
}

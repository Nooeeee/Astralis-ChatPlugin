package me.nooeee.handler;

import me.nooeee.configuration.Config;

public enum MessageType {
    GLOBAL(ConfigKey.GLOBAL_FORMAT),
    LOCAL(ConfigKey.LOCAL_FORMAT),
    OTHER(null);

    private final ConfigKey configKey;

    MessageType(ConfigKey configKey) {
        this.configKey = configKey;
    }

    public String getFormat() {
        return(configKey != null) ? Config.settings.getString(configKey.key) : null;
    }

    // enum pour la configuration
    private enum ConfigKey {
        GLOBAL_FORMAT("global-format"),
        LOCAL_FORMAT("local-format");

        final String key;
        ConfigKey(String key) {
            this.key = key;
        }

    }
}

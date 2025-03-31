package me.nooeee.command;

public enum Pole {
    ANIM("anim", "§a[Animation]", "annonce.animation"),
    MOD("modo", "§6[Modérateur]", "annonce.moderateur"),
    SCENARISTE("scenar", "§9[Scénariste]", "annonce.scenariste");

    private final String commandName;
    final String prefix;
    final String permission;

    Pole(String commandName, String prefix, String permission) {
        this.commandName = commandName;
        this.prefix = prefix;
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    public static Pole fromCommand(String command) {
        for(Pole pole : values()) {
            if(command.equalsIgnoreCase("annonce" + pole.commandName)) {
                return pole;
            }
        }
        return null;
    }
}

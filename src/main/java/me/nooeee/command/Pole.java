package me.nooeee.command;

public enum Pole {
    ANIM("animation", "§a[Animation]", "annonce.animateur"),
    MOD("moderation", "§6[Modérateur]", "annonce.moderateur"),
    SCENARISTE("scenariste", "§9[Scénariste]", "annonce.scenariste");

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

    public String getCommandName() {
        return this.commandName;
    }

    public static Pole fromCommand(String command) {
        for(Pole pole : values()) {
            if(command.equalsIgnoreCase("annonce-" + pole.commandName)) {
                return pole;
            }
        }
        return null;
    }
}

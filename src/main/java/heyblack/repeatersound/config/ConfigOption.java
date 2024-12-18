package heyblack.repeatersound.config;

public enum ConfigOption {
    BASE_PITCH("base_pitch", "0.5"),
    VOLUME("volume", "0.3"),
    USE_RANDOM("use_random", "false"),
    INTERACTION_MODE("interaction_mode", "NORMAL"),
    ALARM_MESSAGE("alarm_message", "Clicked {Block} At: {Pos}"),
    DISABLED_MESSAGE("disabled_message", "Interaction cancelled by RSMod");

    public final String id;
    public final String defaultValue;

    ConfigOption(String id, String defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }
}

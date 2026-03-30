package heyblack.repeatersound.config;

import heyblack.repeatersound.RepeaterSound;

public enum ConfigOption {
    VERSION("version", RepeaterSound.MOD_VERSION),
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

    /**
     * Resolves a config option by its persisted key id.
     *
     * @param id config key in the json file
     * @return matching option, or null when unknown
     */
    public static ConfigOption byId(String id) {
        for (ConfigOption option : values()) {
            if (option.id.equals(id)) {
                return option;
            }
        }
        return null;
    }
}

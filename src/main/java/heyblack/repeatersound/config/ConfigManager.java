package heyblack.repeatersound.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.util.InteractionMode;
import heyblack.repeatersound.util.ServerCloseCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigManager implements ServerCloseCallback
{
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(
        "repeatersound.json"
    );
    private Map<String, String> config = new LinkedHashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private boolean changed = false;

    private static final ConfigManager INSTANCE = new ConfigManager();
    public static ConfigManager getInstance()
    {
        return INSTANCE;
    }

    public ConfigManager()
    {
        // initialize config
        try
        {
            if (Files.exists(CONFIG_PATH))
            {
                // read existing config file
                RepeaterSound.info("Found config file");
                String content = new String(Files.readAllBytes(CONFIG_PATH));
                config = fixConfig(GSON.fromJson(content, Map.class));
            }
            else
            {
                // create or update config file
                RepeaterSound.info("Missing correct config file, trying to create or update");
                config = ConfigUpdater.update();
                Files.write(CONFIG_PATH, GSON.toJson(fixConfig(config)).getBytes());
            }
            RepeaterSound.info("Validating config options...");

            if (validateStoredConfig()) {
                Files.write(CONFIG_PATH, GSON.toJson(config).getBytes());
            }

            RepeaterSound.info("Config file initialized");
        }
        catch (IOException e)
        {
            RepeaterSound.error("Failed to initialize config file!");
            e.printStackTrace();
        }
    }

    public String getConfig(String key)
    {
        return config.get(key);
    }

    /**
     * Applies a config change requested by client command input.
     * Input is normalized with the same validator used by startup and config screen updates.
     */
    public int setConfigCommand(String key, String value, PlayerEntity player)
    {
        ConfigOption option = ConfigOption.byId(key);
        if (option == null || option == ConfigOption.VERSION) {
            return 0;
        }

        String normalized = normalizeUserInput(option, value);
        if (normalized == null) {
            player.sendMessage(Text.of("Invalid value!"), false);
            return 0;
        }

        String prev = config.get(option.id);
        setConfigValue(option.id, normalized);

        switch (option) {
            case BASE_PITCH:
                player.sendMessage(Text.of("Changed basePitch: " + prev + " -> " + normalized +
                        " (default: " + ConfigOption.BASE_PITCH.defaultValue + ")"), false);
                return 1;
            case VOLUME:
                player.sendMessage(Text.of("Changed volume: " + prev + " -> " + normalized +
                        " (default: " + ConfigOption.VOLUME.defaultValue + ")"), false);
                return 1;
            case INTERACTION_MODE:
                player.sendMessage(Text.of("Interaction mode is set to " + normalized), false);
                return 1;
            case USE_RANDOM:
                player.sendMessage(Text.of(Boolean.parseBoolean(normalized) ? "Random pitch offset ON" : "Random pitch offset OFF"), false);
                return 1;
            case ALARM_MESSAGE:
                player.sendMessage(Text.of("Alarm message is set to: " + normalized), false);
                return 1;
            case DISABLED_MESSAGE:
                player.sendMessage(Text.of("Disabled message is set to: " + normalized), false);
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Applies a config change from the config screen.
     * Invalid values are ignored to preserve the last valid config state.
     */
    public void setConfigScreen(String key, String value) {
        ConfigOption option = ConfigOption.byId(key);
        if (option == null || option == ConfigOption.VERSION) {
            return;
        }

        String normalized = normalizeUserInput(option, value);
        if (normalized != null) {
            setConfigValue(option.id, normalized);
        }
    }

    public Map<String, String> fixConfig(Map<String, String> cfgToCheck) throws IOException {
        if (cfgToCheck == null) {
            cfgToCheck = new LinkedHashMap<>();
        }

        Map<String, String> checker = new LinkedHashMap<>();

        for (ConfigOption option : ConfigOption.values()) {
            checker.put(option.id, option.defaultValue);
        }

        boolean bl = false;

        for (Map.Entry<String, String> checkerEntry : checker.entrySet())
        {
            if (!cfgToCheck.containsKey(checkerEntry.getKey()))
            {
                cfgToCheck.put(checkerEntry.getKey(), checkerEntry.getValue());
                RepeaterSound.warn("Missing config option: " +
                checkerEntry.getKey() + ", added with default value: " + checkerEntry.getValue());

                bl = true;
            }
        }

        if (bl) {
            Files.write(CONFIG_PATH, GSON.toJson(cfgToCheck).getBytes());
        }

        return cfgToCheck;
    }

    /**
     * Writes the value only when it actually changes, so save-on-close can be skipped for no-op updates.
     */
    private void setConfigValue(String key, String value) {
        String prev = config.get(key);
        if (prev == null || !prev.equals(value)) {
            config.put(key, value);
            changed = true;
        }
    }

    /**
     * Validates every persisted option and replaces invalid entries with defaults.
     *
     * @return true when at least one entry was repaired
     */
    private boolean validateStoredConfig() {
        boolean updated = false;

        for (ConfigOption option : ConfigOption.values()) {
            String current = config.get(option.id);
            String normalized = normalizeStoredValue(option, current);
            if (current == null || !current.equals(normalized)) {
                config.put(option.id, normalized);
                updated = true;
            }
        }

        return updated;
    }

    /**
     * Normalizes a persisted value and falls back to default with a warning when invalid.
     */
    private String normalizeStoredValue(ConfigOption option, String rawValue) {
        String normalized = normalizeUserInput(option, rawValue);
        if (normalized != null) {
            return normalized;
        }

        RepeaterSound.warn("Invalid value found for config option " + option.id +
                ". Replaced with default value: " + option.defaultValue);
        return option.defaultValue;
    }

    /**
     * Normalizes raw user input according to the option type.
     *
     * <p>Returns null when the value is invalid for this option.</p>
     */
    private String normalizeUserInput(ConfigOption option, String rawValue) {
        if (rawValue == null) {
            return null;
        }

        switch (option) {
            case BASE_PITCH:
            case VOLUME:
                try {
                    return String.valueOf(Float.parseFloat(rawValue));
                } catch (NumberFormatException e) {
                    return null;
                }
            case INTERACTION_MODE:
                try {
                    return InteractionMode.valueOf(rawValue.toUpperCase()).toString();
                } catch (IllegalArgumentException e) {
                    return null;
                }
            case USE_RANDOM:
                if ("true".equals(rawValue) || "false".equals(rawValue)) {
                    return rawValue;
                }
                return null;
            case ALARM_MESSAGE:
            case DISABLED_MESSAGE:
                return rawValue;
            case VERSION:
            default:
                return null;
        }
    }

    public String getAlarmMessage(BlockState state, BlockPos pos)
    {
        return getConfig(ConfigOption.ALARM_MESSAGE.id)
                .replace("{Block}", state.getBlock().toString())
                .replace("{Pos}", pos.toShortString());
    }

    @Override
    public void saveConfig() 
    {
        if (changed) 
        {
            try
            {
                RepeaterSound.info("Writing config to file");
                Map<String, String> cfgToSave = new LinkedHashMap<>();

                for (ConfigOption option : ConfigOption.values()) {
                    Optional<String> value = Optional.ofNullable(config.get(option.id));
                    cfgToSave.put(option.id, value.orElse(option.defaultValue));
                }

                Files.write(CONFIG_PATH, GSON.toJson(cfgToSave).getBytes());
            }
            catch (IOException e)
            {
                RepeaterSound.error("Failed to write config to file!");
                e.printStackTrace();
            }
        }
    }
}
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
            boolean bl = false;

            for (Map.Entry<String, String> entry : config.entrySet()) {
                switch (entry.getKey()) {
                    case "base_pitch":
                        try {
                            Float.parseFloat(entry.getValue());
                        } catch (NullPointerException | NumberFormatException e) {
                            entry.setValue(ConfigOption.BASE_PITCH.defaultValue);
                            RepeaterSound.warn("Invalid value found for config option " + entry.getKey() + ". Replaced with default value: " + ConfigOption.BASE_PITCH.defaultValue);
                            bl = true;
                        }
                        break;
                    case "volume":
                        try {
                            Float.parseFloat(entry.getValue());
                        } catch (NullPointerException | NumberFormatException e) {
                            entry.setValue(ConfigOption.VOLUME.defaultValue);
                            RepeaterSound.warn("Invalid value found for config option " + entry.getKey() + ". Replaced with default value: " + ConfigOption.VOLUME.defaultValue);
                            bl = true;
                        }
                        break;
                    case "interaction_mode":
                        try {
                            InteractionMode.valueOf(entry.getValue());
                        } catch (NullPointerException | IllegalArgumentException e) {
                            entry.setValue(ConfigOption.INTERACTION_MODE.defaultValue);
                            RepeaterSound.warn("Invalid value found for config option " + entry.getKey() + ". Replaced with default value: " + ConfigOption.INTERACTION_MODE.defaultValue);
                            bl = true;
                        }
                }
            }
            if (bl) {
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

    public int setConfigCommand(String key, String value, PlayerEntity player)
    {
        String prev = config.get(key);
        switch (key) {
            case "base_pitch":
            case "volume":
                try {
                    Float.parseFloat(value);

                    if (key.equals("base_pitch")) {
                        player.sendMessage(Text.of("Changed basePitch: " + prev + " -> " + value +
                        " (default: 0.5)"), false);
                        config.put(key, value);
                        changed = true;

                        return 1;
                    }

                    player.sendMessage(Text.of("Changed volume: " + prev + " -> " + value +
                    " (default: 0.3)"), false);
                    config.put(key, value);
                    changed = true;

                    return 1;

                } catch (NumberFormatException e) {
                    player.sendMessage(Text.of("Invalid value!"), false);
                }
                break;

            case "interaction_mode":
                try {
                    String value1 = InteractionMode.valueOf(value.toUpperCase()).toString();
                    player.sendMessage(Text.of("Interaction mode is set to " + value), false);
                    config.put(key, value1);
                    changed = true;

                    return 1;

                } catch (IllegalArgumentException e) {
                    player.sendMessage(Text.of("Invalid value!"), false);
                }
                break;
            
            case "use_random":
                if (value.equals("true")) {
                    player.sendMessage(Text.of("Random pitch offset ON"), false);
                    config.put(key, value);
                    changed = true;

                    return 1;
                }

                if (value.equals("false")) {
                    player.sendMessage(Text.of("Random pitch offset OFF"), false);
                    config.put(key, value);
                    changed = true;

                    return 1;
                }

                player.sendMessage(Text.of("Invalid value!"), false);
                break;
            
            case "alarm_message":
            player.sendMessage(Text.of("Alarm message is set to: " + value), false);
                config.put(key, value);
                changed = true;

                return 1;

            case "disabled_message":
                player.sendMessage(Text.of("Disabled message is set to: " + value), false);
                config.put(key, value);
                changed = true;

                return 1;
        }

        return 0;
    }

    public void setConfigScreen(String key, String value) {
        switch (key) {
            case "base_pitch":
            case "volume":
                try {
                    Float.parseFloat(value);

                    if (key.equals("basePitch")) {
                        config.put(key, value);
                        changed = true;

                        break;
                    }

                    config.put(key, value);
                    changed = true;
                } catch (NumberFormatException e) {
                }
                break;

            case "interaction_mode":
                try {
                    String value1 = InteractionMode.valueOf(value.toUpperCase()).toString();
                    config.put(key, value1);
                    changed = true;
                } catch (IllegalArgumentException e) {
                }
                break;

            case "use_random":
                if (value.equals("true")) {
                    config.put(key, value);
                    changed = true;

                    break;
                }

                if (value.equals("false")) {
                    config.put(key, value);
                    changed = true;

                    break;
                }
                break;

            case "alarm_message":
            case "disabled_message":
                config.put(key, value);
                changed = true;
                break;
        }
    }

    public Map<String, String> fixConfig(Map<String, String> cfgToCheck) throws IOException {
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
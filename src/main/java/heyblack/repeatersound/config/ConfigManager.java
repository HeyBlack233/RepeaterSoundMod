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

public class ConfigManager implements ServerCloseCallback
{
    private Path path = FabricLoader.getInstance().getConfigDir().resolve(
        "repeatersound.json"
    );
    private Map<String, String> config = new LinkedHashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private boolean changed = false;

    private static ConfigManager instance = new ConfigManager();
    public static ConfigManager getInstance()
    {
        return instance;
    }

    public ConfigManager()
    {
        // initialize config
        try
        {
            if (Files.exists(path))
            {
                // read existing config file
                RepeaterSound.info("Found config file");
                String content = new String(Files.readAllBytes(path));
                config = fixConfig(GSON.fromJson(content, Map.class));
            }
            else
            {
                // create or update config file
                RepeaterSound.info("Missing correct config file, trying to create or update");
                config = ConfigUpdater.update();
                Files.write(path, GSON.toJson(fixConfig(config)).getBytes());
                RepeaterSound.info("Config file initialized");
            }
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

    public int setConfig(String key, String value, PlayerEntity player)
    {
        String prev = config.get(key);
        switch (key) {
            case "basePitch":
            case "volume":
                try {
                    Float.parseFloat(value);

                    if (key.equals("basePitch")) {
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

            case "interactionMode":
                try {
                    value = InteractionMode.valueOf(value.toUpperCase()).toString();
                    player.sendMessage(Text.of("Interaction mode is set to " + value), false);
                    config.put(key, value);
                    changed = true;

                    return 1;

                } catch (IllegalArgumentException e) {
                    player.sendMessage(Text.of("Invalid value!"), false);
                }
                break;
            
            case "useRandom":
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
            
            case "alarmMessage":
            player.sendMessage(Text.of("Alarm message is set to: " + value), false);
                config.put(key, value);
                changed = true;

                return 1;

            case "disabledMessage":
                player.sendMessage(Text.of("Disabled message is set to: " + value), false);
                config.put(key, value);
                changed = true;

                return 1;
        }

        return 0;
    }

    public Map<String, String> fixConfig(Map<String, String> cfgToCheck)
    {
        Map<String, String> checker = new LinkedHashMap<>();
        checker.put("version", RepeaterSound.MOD_VERSION);
        checker.put("basePitch", "0.5");
        checker.put("volume", "0.3");
        checker.put("useRandom", "false");
        checker.put("interactionMode", "NORMAL");
        checker.put("alarmMessage", "Clicked {Block} At: {Pos}");
        checker.put("disabledMessage", "Interaction cancelled by RSMod");

        for (Map.Entry<String, String> entry : checker.entrySet())
        {
            if (!cfgToCheck.containsKey(entry.getKey()))
            {
                cfgToCheck.put(entry.getKey(), entry.getValue());
                RepeaterSound.warn("Missing config option: " +
                entry.getKey() + ", added with default value: " + entry.getValue());
            }
        }

        return cfgToCheck;
    }

    public String getAlarmMessage(BlockState state, BlockPos pos)
    {
        return getConfig("alarmMessage")
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
                Files.write(path, GSON.toJson(config).getBytes());
            }
            catch (IOException e)
            {
                RepeaterSound.error("Failed to write config to file!");
                e.printStackTrace();
            }
        }
    }
}
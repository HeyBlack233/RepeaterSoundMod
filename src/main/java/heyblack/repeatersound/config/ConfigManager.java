package heyblack.repeatersound.config;

import com.google.gson.Gson;
import heyblack.repeatersound.RepeaterSound;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager
{
    Logger logger = LogManager.getLogger();

    private Path path = FabricLoader.getInstance().getConfigDir().resolve("repeatersound" + RepeaterSound.MOD_VERSION + ".json");
    private Map<String, String> config = new HashMap<>();
    private Gson gson = new Gson();

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
                logger.info("[RepeaterSound] Found config file");
                String content = new String(Files.readAllBytes(path));
                config = fixConfig(gson.fromJson(content, Map.class));
            }
            else
            {
                // create or update config file
                logger.info("[RepeaterSound] Missing correct config file, trying to create or update");
                config = ConfigUpdater.update();
                Files.write(path, gson.toJson(fixConfig(config)).getBytes());
                logger.info("[RepeaterSound] Config file initialized");
            }
        }
        catch (IOException e)
        {
            logger.error("[RepeaterSound] Failed to initialize config file!");
            e.printStackTrace();
        }
    }

    public String getConfig(String key)
    {
        return config.get(key);
    }

    public int setConfig(String key, String value)
    {
        config.put(key, value);
        try
        {
            logger.info("[RepeaterSound] Writing config to file: {" + key + ": " + value + "}");
            Files.write(path, gson.toJson(config).getBytes());
            return 1;
        }
        catch (IOException e)
        {
            logger.error("[RepeaterSound] Failed to write config to file!");
            e.printStackTrace();
            return 0;
        }
    }

    public Map<String, String> fixConfig(Map<String, String> cfgToCheck)
    {
        Map<String, String> checker = new HashMap<>();
        checker.put("basePitch", "0.5");
        checker.put("volume", "0.3");
        checker.put("useRandomPitch", "false");
        checker.put("interactionMode", "NORMAL");

        for (Map.Entry<String, String> entry : checker.entrySet())
        {
            if (!cfgToCheck.containsKey(entry.getKey()))
            {
                cfgToCheck.put(entry.getKey(), entry.getValue());
                logger.warn("[RepeaterSound] Missing config option: " + entry.getKey() + ", added with default value: " + entry.getValue());
            }
        }

        return cfgToCheck;
    }
}
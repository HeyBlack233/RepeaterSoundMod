package heyblack.repeatersound.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ConfigManager
{
    Logger logger = LogManager.getLogger();
    Config cfg = new Config();
    static Gson gson = new Gson();
    public File cfgFile = FabricLoader.getInstance().getConfigDir().resolve("repeatersound.json5").toFile();

    private static ConfigManager instance = new ConfigManager();
    public static ConfigManager getInstance()
    {
        return instance;
    }

    public void loadConfig()
    {
        if(!cfgFile.exists())
        {
            try
            {
                cfgFile.createNewFile();
                cfg.setDefault();
                save(cfg);

                return;
            }
            catch (IOException e)
            {
                logger.error("[RepeaterSound] " + "error while creating config!");
                throw new RuntimeException(e);
            }
        }
        cfg = getConfigFromFile();
    }

    public void save(Config cfg)
    {
        logger.info("[RepeaterSound] " + "saving config to file: [basePitch:{}, useRandom:{}]", cfg.getBasePitch(), cfg.getRandomPitch());
        String str = gson.toJson(cfg);
        try
        {
            FileWriter writer = new FileWriter(cfgFile.getAbsolutePath());
            writer.write(str);
            writer.close();
        }
        catch (IOException e)
        {
            logger.error("[RepeaterSound] " + "error while saving config!");
            throw new RuntimeException(e);
        }
    }

    public Config getConfigFromFile()
    {
        try
        {
            JsonReader reader = new JsonReader(new FileReader(cfgFile));
            return gson.fromJson(reader, Config.class);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}